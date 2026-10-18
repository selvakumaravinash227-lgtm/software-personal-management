package edu.spms.web.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name="leave_requests")
public class LeaveEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="leave_id") public Long id;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="employee_id",nullable=false) public EmployeeEntity employee;
    @Column(name="start_date",nullable=false) public LocalDate startDate;
    @Column(name="end_date",nullable=false) public LocalDate endDate;
    @Column(name="leave_type",nullable=false,length=40) public String leaveType;
    @Column(nullable=false,length=500) public String reason;
    @Column(nullable=false,length=10) public String status="PENDING";
    @Column(name="requested_at",insertable=false,updatable=false) public LocalDateTime requestedAt;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="reviewed_by") public UserEntity reviewer;
    @Column(name="reviewed_at") public LocalDateTime reviewedAt;
    @Column(name="decision_note",nullable=false,length=255) public String decisionNote="";
}
