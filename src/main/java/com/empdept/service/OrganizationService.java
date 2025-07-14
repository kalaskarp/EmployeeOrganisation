package com.empdept.service;

import com.empdept.entity.Department;
import com.empdept.entity.Employee;
import com.empdept.repository.DepartmentRepository;
import com.empdept.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private DepartmentRepository departmentRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    public OrganizationService(DepartmentRepository departmentRepo, EmployeeRepository employeeRepo) {
        this.departmentRepo = departmentRepo;
        this.employeeRepo = employeeRepo;
    }

    public void saveDepartments(List<Department> departments) {
        for (Department dept : departments) {
            for (Employee emp : dept.getEmployees()) {
                emp.setDepartment(dept); // set back-reference
            }
            departmentRepo.save(dept);
        }
    }

    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

    public List<Department> getAllDepartments() {
        return departmentRepo.findAll();
    }

    public List<Employee> getEmployeesByDepartment(String deptId) {
        return employeeRepo.findByDepartment_Id(deptId);
    }

    public boolean addEmployeeToDepartment(String deptId, Employee employee) {
        Optional<Department> departmentOpt = departmentRepo.findById(deptId);
        if (departmentOpt.isPresent()) {
            employee.setDepartment(departmentOpt.get());
            employeeRepo.save(employee);
            return true;
        }
        return false;
    }

    public boolean deleteEmployeeFromDepartment(String empId) {
        if (employeeRepo.existsById(empId)) {
            employeeRepo.deleteById(empId);
            return true;
        }
        return false;
    }

    public boolean addDepartment(Department department) {
        if (!departmentRepo.existsById(department.getId())) {
            departmentRepo.save(department);
            return true;
        }
        return false;
    }
}
