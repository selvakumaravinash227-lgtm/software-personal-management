package edu.spms.web.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity @Table(name="attendance",uniqueConstraints=@UniqueConstraint(name="uq_attendance_employee_date",columnNames={"employee_id","work_date"}))
public class AttendanceEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="attendance_id") public Long id;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="employee_id",nullable=false) public EmployeeEntity employee;
    @Column(name="work_date",nullable=false) public LocalDate workDate;
    @Column(nullable=false,length=10) public String status;
    @Column(name="check_in") public LocalTime checkIn;
    @Column(name="check_out") public LocalTime checkOut;
    @Column(nullable=false,length=255) public String notes="";
}
