package edu.spms.web.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="users")
public class UserEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="user_id") public Integer id;
    @Column(nullable=false,unique=true,length=60) public String username;
    @Column(name="password_hash",nullable=false,length=255) public String passwordHash;
    @Column(nullable=false,length=20) public String role;
    @OneToOne(fetch=FetchType.EAGER) @JoinColumn(name="employee_id",unique=true) public EmployeeEntity employee;
    @Column(nullable=false) public boolean active=true;
    @Column(name="must_change_password",nullable=false) public boolean mustChangePassword=false;
    @Column(name="created_at",insertable=false,updatable=false) public LocalDateTime createdAt;
}
