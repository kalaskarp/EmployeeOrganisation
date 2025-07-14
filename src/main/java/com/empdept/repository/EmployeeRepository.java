package com.empdept.repository;

import com.empdept.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    List<Employee> findByDepartment_Id(String deptId);
}