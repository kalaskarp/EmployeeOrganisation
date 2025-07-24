package com.empdept.repository;

import com.empdept.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, String> {
    @EntityGraph(attributePaths = {"employees"})
    List<Department> findAll();
}