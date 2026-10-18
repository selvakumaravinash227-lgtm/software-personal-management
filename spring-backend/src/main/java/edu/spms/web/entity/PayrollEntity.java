package edu.spms.web.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name="payroll",uniqueConstraints=@UniqueConstraint(name="uq_payroll_employee_month",columnNames={"employee_id","pay_month"}))
public class PayrollEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="payroll_id") public Long id;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="employee_id",nullable=false) public EmployeeEntity employee;
    @Column(name="pay_month",nullable=false) public LocalDate payMonth;
    @Column(name="basic_salary",nullable=false,precision=12,scale=2) public BigDecimal basicSalary;
    @Column(nullable=false,precision=12,scale=2) public BigDecimal allowances=BigDecimal.ZERO;
    @Column(nullable=false,precision=12,scale=2) public BigDecimal deductions=BigDecimal.ZERO;
    @Column(name="net_salary",insertable=false,updatable=false,precision=13,scale=2) public BigDecimal netSalary;
    @Column(nullable=false,length=10) public String status="DRAFT";
    @Column(name="pay_date") public LocalDate payDate;
}
