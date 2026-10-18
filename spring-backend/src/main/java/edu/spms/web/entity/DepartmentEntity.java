package edu.spms.web.entity;

import jakarta.persistence.*;

@Entity @Table(name="departments")
public class DepartmentEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="department_id") public Integer id;
    @Column(nullable=false,unique=true,length=80) public String name;
    @Column(nullable=false,length=255) public String description="";
}
