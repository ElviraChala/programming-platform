package com.elvira.programming_platform.config;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.BatchV1Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class KubernetesConfig {

    private static final Logger log = LoggerFactory.getLogger(KubernetesConfig.class);

    @Bean
    public ApiClient apiClient() throws IOException {
        // Автоматично налаштовує клієнт з конфігурації ~/.kube/config
        // Для роботи всередині Kubernetes кластера використовуйте Config.fromCluster()
        try {
            // Спочатку пробуємо завантажити конфігурацію з кластера
            log.info("Trying to load in-cluster config...");
            ApiClient apiClient = Config.fromCluster();
            log.info("In-cluster config loaded successfully!");
            return apiClient;
        } catch (Exception e) {
            // Якщо не вийшло (наприклад, працюємо локально), використовуємо дефолтний конфіг
            String message = "Failed to load in-cluster config, trying default config. Error: " + e.getMessage();
            log.warn(message);
            return Config.defaultClient();

        }
    }

    @Bean
    public CoreV1Api coreV1Api(ApiClient apiClient) {
        return new CoreV1Api(apiClient);
    }

    @Bean
    public BatchV1Api batchV1Api(ApiClient apiClient) {
        return new BatchV1Api(apiClient);
    }

    @Bean
    public io.kubernetes.client.openapi.ApiClient kubernetesApiClient() throws IOException {
        return Config.defaultClient();
    }
}
