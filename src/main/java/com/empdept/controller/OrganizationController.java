package com.empdept.controller;

import com.empdept.DTO.DepartmentListWrapper;
import com.empdept.entity.Department;
import com.empdept.entity.Employee;
import com.empdept.report.ReportService;
import com.empdept.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrganizationController {

    @Autowired
    OrganizationService service;

    @Autowired
    private ReportService reportService;

    // a. Get all employees
    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return service.getAllEmployees();
    }

    // b. Get all departments
    @GetMapping("/departments")
    public List<Department> getAllDepartments() {
        return service.getAllDepartments();
    }

    // c. Get employees in a department
    @GetMapping("/departments/{deptId}/employees")
    public List<Employee> getEmployeesInDepartment(@PathVariable String deptId) {
        return service.getEmployeesByDepartment(deptId);
    }

    // d. Add new employee to department
    @PostMapping("/departments/{deptId}/employees")
    public ResponseEntity<String> addEmployee(@PathVariable String deptId, @RequestBody Employee employee) {
        boolean added = service.addEmployeeToDepartment(deptId, employee);
        if (added) {
            return ResponseEntity.ok("Employee added to department " + deptId);
        }
        return ResponseEntity.badRequest().body("Department not found");
    }

    @PostMapping("/departments")
    public ResponseEntity<String> saveAllDepartments(@RequestBody DepartmentListWrapper wrapper) {
        service.saveDepartments(wrapper.getDepartments());
        return ResponseEntity.ok("Departments and Employees saved successfully.");
    }

    // delete employee
    @DeleteMapping("/departments/{deptId}/employees/{empId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String deptId, @PathVariable String empId) {
        boolean removed = service.deleteEmployeeFromDepartment(empId);
        if (removed) {
            return ResponseEntity.ok("Employee removed");
        }
        return ResponseEntity.badRequest().body("Employee not found");
    }

    // jasper report
    @GetMapping("/employees-per-department")
    public ResponseEntity<byte[]> downloadReport() throws Exception {
        byte[] pdf = reportService.generateEmployeeDepartmentReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("DepartmentEmployeeReport.pdf").build());

        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
