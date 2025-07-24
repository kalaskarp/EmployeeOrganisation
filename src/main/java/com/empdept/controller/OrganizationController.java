package com.empdept.controller;

import com.empdept.DTO.DepartmentListWrapper;
import com.empdept.entity.Department;
import com.empdept.entity.Employee;
import com.empdept.exception.ResourceNotFoundException;
import com.empdept.report.ReportService;
import com.empdept.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
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
        System.out.println("into gertEmployeesInDepartment");
        return service.getEmployeesByDepartment(deptId);
    }

    // d. Add new employee to department
    @PostMapping("/departments/{deptId}/employees")
    public ResponseEntity<?> addEmployee(@PathVariable String deptId, @RequestBody Employee employee) {
        try {
            Employee savedEmployee = service.addEmployeeToDepartment(deptId, employee);
            return ResponseEntity.ok(savedEmployee);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error adding employee: " + e.getMessage());
        }
    }

    @PostMapping("/departments")
    public ResponseEntity<String> saveAllDepartments(@RequestBody DepartmentListWrapper wrapper) {
        service.saveDepartments(wrapper.getDepartments());
        return ResponseEntity.ok("Departments and Employees saved successfully.");
    }

    // delete employee
    @DeleteMapping("/departments/employees/{empId}")
    public ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable String empId) {
        Map<String, String> response = new HashMap<>();
        try {
            if (service.deleteEmployeeFromDepartment(empId)) {
                response.put("message", "Employee with ID " + empId + " has been deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Employee with ID " + empId + " not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("error", "An error occurred while deleting employee: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
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
