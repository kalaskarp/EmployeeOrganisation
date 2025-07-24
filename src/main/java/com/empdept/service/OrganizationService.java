package com.empdept.service;

import com.empdept.entity.Department;
import com.empdept.entity.Employee;
import com.empdept.exception.ResourceNotFoundException;
import com.empdept.repository.DepartmentRepository;
import com.empdept.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.lang.Exception;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

/**
 * Service class for handling organization-related operations including department and employee management.
 * Provides caching and transactional support for better performance and data consistency.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = {"departments", "employees"})
@Transactional(readOnly = true)

public class OrganizationService {

    private final DepartmentRepository departmentRepo;
    private final EmployeeRepository employeeRepo;


    /**
     * Saves multiple departments with their employees in a batch operation.
     * @param departments List of departments to save
     * @return List of saved departments
     * @throws DataAccessException if there's an issue accessing the database
     */
    @Transactional
    @CacheEvict(allEntries = true)
    public List<Department> saveDepartments(List<Department> departments) {
        try {
            departments.forEach(dept -> {
                if (dept.getEmployees() != null) {
                    dept.getEmployees().forEach(emp -> emp.setDepartment(dept));
                }
            });
            return departmentRepo.saveAll(departments);
        } catch (DataAccessException ex) {
            log.error("Error saving departments: {}", ex.getMessage(), ex);
            throw new DataAccessException("Failed to save departments: " + ex.getMessage(), ex) {};
        }
    }

    /**
     * Retrieves all employees with caching support.
     * @return List of all employees
     */
    @Cacheable(key = "'all-employees'")
    public List<Employee> getAllEmployees() {
        log.debug("Fetching all employees");
        return employeeRepo.findAll();
    }

    /**
     * Retrieves all departments with their associated employees using caching.
     * @return List of all departments
     */
    @Cacheable(key = "'all-departments'")
    public List<Department> getAllDepartments() {
        log.debug("Fetching all departments");
        return departmentRepo.findAll();
    }

    /**
     * Retrieves all employees for a specific department with caching support.
     * @param deptId The department ID
     * @return List of employees in the specified department
     * @throws ResourceNotFoundException if department is not found
     */
    @Cacheable(key = "'dept-' + #deptId")
    public List<Employee> getEmployeesByDepartment(String deptId) {
        log.debug("Fetching employees for department: {}", deptId);
        if (!departmentRepo.existsById(deptId)) {
            throw new ResourceNotFoundException("Department not found with id: " + deptId);
        } else if (isEmpty(employeeRepo.findByDepartment_Id(deptId))) {
            throw new ResourceNotFoundException("Employee not found with id: " + deptId);
        }
        return employeeRepo.findByDepartment_Id(deptId);
    }

    /**
     * Adds a new employee to the specified department.
     * @param deptId The department ID
     * @param employee The employee to add
     * @return The saved employee
     * @throws ResourceNotFoundException if department is not found
     */
    @Transactional
    @CacheEvict(allEntries = true)
    public Employee addEmployeeToDepartment(String deptId, Employee employee) {
        log.debug("Adding employee to department: {}", deptId);
        Department department = departmentRepo.findById(deptId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + deptId));
        
        employee.setDepartment(department);
        return employeeRepo.save(employee);
    }
    /**
     * Deletes an employee from the system.
     * @param empId The employee ID to delete
     * @throws ResourceNotFoundException if employee is not found
     */
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean deleteEmployeeFromDepartment(String empId) {
        log.debug("Deleting employee with id: {}", empId);
        try {
            if (employeeRepo.existsById(empId)) {
                employeeRepo.deleteById(empId);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Error deleting employee with id {}: {}", empId, e.getMessage());
            return false;
        }
    }
    /**
     * Adds a new department to the system.
     * @param department The department to add
     * @return The saved department
     * @throws IllegalArgumentException if department with ID already exists
     */
    @Transactional
    @CacheEvict(allEntries = true)
    public Department addDepartment(Department department) {
        log.debug("Adding new department: {}", department.getName());
        String deptId = department.getId();
        if (deptId != null && departmentRepo.existsById(deptId)) {
            throw new IllegalArgumentException("Department with ID " + deptId + " already exists");
        }
        return departmentRepo.save(department);
    }
}
