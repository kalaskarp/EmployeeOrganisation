package com.empdept.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    private String id;

    private String name;
    private String email;
    private String position;
    private double salary;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonBackReference
    private Department department;

}
