package com.empdept.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "department")
public class Department {

    @Id
    private String id;

    private String name;
    private String location;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Employee> employees;
}
