package edu.spms.web.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name="employees")
public class EmployeeEntity {
    @Id @Column(name="employee_id",length=16) public String id;
    @Column(nullable=false,length=120) public String name;
    @Column(nullable=false,unique=true,length=160) public String email;
    @Column(nullable=false,length=24) public String phone;
    @Column(nullable=false,length=24) public String gender="Prefer not to say";
    @Column(name="date_of_birth") public LocalDate dateOfBirth;
    @Column(nullable=false,length=255) public String address="";
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="department_id") public DepartmentEntity department;
    @Column(nullable=false,length=100) public String designation;
    @Column(name="joining_date",nullable=false) public LocalDate joiningDate;
    @Column(nullable=false,precision=12,scale=2) public BigDecimal salary;
    @Column(name="employment_status",nullable=false,length=10) public String employmentStatus="ACTIVE";
}
