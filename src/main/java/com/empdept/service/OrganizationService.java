package com.empdept.service;

import com.empdept.entity.Department;
import com.empdept.entity.Employee;
import com.empdept.repository.DepartmentRepository;
import com.empdept.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private DepartmentRepository departmentRepo;

    @Autowired
    private EmployeeRepository employeeRepo;
    

    @CacheEvict(value = { "allDepartments", "allEmployees", "employeesByDepartment" }, allEntries = true)
    public void saveDepartments(List<Department> departments) {
        for (Department dept : departments) {
            for (Employee emp : dept.getEmployees()) {
                emp.setDepartment(dept); // set back-reference
            }
            departmentRepo.save(dept);
        }
    }

    @Cacheable(value = "allEmployees")
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    @Cacheable(value = "allDepartments")
    public List<Department> getAllDepartments() {

        return departmentRepo.findAll();
    }

    @Cacheable(value = "employeesByDepartment", key = "#deptId")
    public List<Employee> getEmployeesByDepartment(String deptId) {

        return employeeRepo.findByDepartment_Id(deptId);
    }

    @CacheEvict(value = { "allEmployees", "employeesByDepartment" }, allEntries = true)
    public boolean addEmployeeToDepartment(String deptId, Employee employee) {
        Optional<Department> departmentOpt = departmentRepo.findById(deptId);
        if (departmentOpt.isPresent()) {
            employee.setDepartment(departmentOpt.get());
            employeeRepo.save(employee);
            return true;
        }
        return false;
    }
    @CacheEvict(value = { "allEmployees", "employeesByDepartment", "employee" }, allEntries = true)
    public boolean deleteEmployeeFromDepartment(String empId) {
        if (employeeRepo.existsById(empId)) {
            employeeRepo.deleteById(empId);
            return true;
        }
        return false;
    }
    @CacheEvict(value = "allDepartments", allEntries = true)
    public boolean addDepartment(Department department) {
        if (!departmentRepo.existsById(department.getId())) {
            departmentRepo.save(department);
            return true;
        }
        return false;
    }
}
