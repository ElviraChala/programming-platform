package com.elvira.programming_platform.service;

import com.elvira.programming_platform.dto.check.InterpreterRequest;
import com.elvira.programming_platform.dto.check.InterpreterResponse;
import com.elvira.programming_platform.model.ProgrammingTask;
import com.elvira.programming_platform.repository.ProgrammingTaskRepository;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterpreterService {

    private final BatchV1Api batchV1Api;
    private final CoreV1Api coreV1Api;
    private final ProgrammingTaskRepository repository;
    private static final String NAMESPACE = "programming-platform";

    public InterpreterResponse run(Long id, InterpreterRequest request) {
        String jobName = "test-job-" + System.currentTimeMillis();
        ProgrammingTask programmingTask = repository.findById(id).orElseThrow();

        // 2. Чекаємо на завершення та отримуємо результати
        try {
            createTestJob(jobName, request.getCode());
            String actualOutput = waitForJobCompletion(jobName, Duration.ofMinutes(5));
            String expectedOutput = programmingTask.getExpectedOutput();

            InterpreterResponse response = new InterpreterResponse();
            response.setActualOutput(actualOutput);
            response.setExpectedOutput(expectedOutput);

            boolean isOk = actualOutput.equals(expectedOutput);

            response.setIsOk(isOk);

            return response;
        } catch (InterruptedException | TimeoutException | ApiException e) {
            throw new RuntimeException(e);
        } finally {
            // Удаляємо Job
            try {
                batchV1Api.deleteNamespacedJob(jobName, NAMESPACE).execute();
            } catch (ApiException ignore) {
                throw new RuntimeException(ignore);
            }
        }

        // 3. Зберігаємо результат (поки не зберігаємо)
        // repository.save(result);
    }

    private String waitForJobCompletion(String jobName, Duration timeout)
            throws InterruptedException, TimeoutException, ApiException {
        Instant start = Instant.now();

        while (true) {
            // Перевіряємо, чи не вийшли за таймаут
            if (Duration.between(start, Instant.now()).compareTo(timeout) > 0) {
                throw new TimeoutException("Job execution timed out");
            }

            // Отримуємо статус Job
            V1Job job = batchV1Api.readNamespacedJob(jobName, NAMESPACE).execute();
            V1JobStatus status = job.getStatus();

            if (status != null) {
                // Job завершена успішно

                return getJobLogs(jobName);
            }

            // Чекаємо перед наступною перевіркою
            TimeUnit.SECONDS.sleep(5);
        }
    }

    private String getJobLogs(String jobName) {
        try {
            // Отримуємо Pod для цього Job
            V1PodList podList = coreV1Api.listNamespacedPod(NAMESPACE).execute();

            if (podList.getItems().isEmpty()) {
                return "No pods found for job";
            }

            V1Pod pod = podList.getItems().stream().filter(p -> {
                assert p.getMetadata() != null;
                assert p.getMetadata().getName() != null;
                return p.getMetadata().getName().startsWith(jobName);
            }).findFirst().orElseThrow();
            assert pod.getMetadata() != null;
            String podName = pod.getMetadata().getName();

            V1PodStatus status = pod.getStatus();
            int i = 0;
            while ((status == null
                    || !List.of("Succeeded",
                    "Failed",
                    "Completed",
                    "Complete",
                    "Error").contains(status.getPhase()))
                    && i < 60) {
                Thread.sleep(1000);
                pod = coreV1Api.readNamespacedPod(podName, NAMESPACE).execute();
                status = pod.getStatus();
                i++;
            }
            assert status != null;
            log.info("Pod {} has status {}", podName, status.getPhase());

            // Отримуємо логи Pod
            return coreV1Api.readNamespacedPodLog(podName, NAMESPACE).execute().trim();

        } catch (Exception e) {
            return "Failed to get logs: " + e.getMessage();
        }
    }

    public void createTestJob(String jobName, String javaCode) {
        // Створюємо контейнер
        V1Container container = new V1Container()
                .name("test-container")
                .image("openjdk:11")
                .command(List.of("sh", "-c",
                        "echo '" + javaCode + "' > Main.java && " +
                                "javac Main.java && " +
                                "java Main"));

        // Створюємо специфікацію Job
        V1JobSpec jobSpec = new V1JobSpec()
                .template(new V1PodTemplateSpec()
                        .spec(new V1PodSpec()
                                .containers(List.of(container))
                                .restartPolicy("Never"))
                )
                .backoffLimit(0);

        // Створюємо Job
        V1Job job = new V1Job()
                .apiVersion("batch/v1")
                .metadata(new V1ObjectMeta()
                        .name(jobName)
                        .namespace(NAMESPACE))
                .spec(jobSpec);

        // Створюємо Job у Kubernetes
        try {
            batchV1Api.createNamespacedJob(NAMESPACE, job).execute();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }
}