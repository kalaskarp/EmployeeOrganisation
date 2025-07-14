package com.empdept.DTO;

import com.empdept.entity.Department;

import java.util.List;

public class DepartmentListWrapper {

    private List<Department> departments;

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }
}
