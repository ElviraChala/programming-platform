package com.elvira.programming_platform.repository;

import com.elvira.programming_platform.model.Admin;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, Long> {
}
