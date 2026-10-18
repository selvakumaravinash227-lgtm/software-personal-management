package edu.spms.web.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="payroll_payments")
public class PayrollPaymentEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="payment_id") public Long id;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="payroll_id",nullable=false) public PayrollEntity payroll;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="initiated_by",nullable=false) public UserEntity initiatedBy;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="confirmed_by",nullable=false) public UserEntity confirmedBy;
    @Column(name="initiated_at",insertable=false,updatable=false) public LocalDateTime initiatedAt;
    @Column(name="confirmed_at",insertable=false,updatable=false) public LocalDateTime confirmedAt;
    @Column(nullable=false,precision=12,scale=2) public BigDecimal amount;
    @Column(name="payment_reference",nullable=false,length=100) public String paymentReference="";
    @Column(nullable=false,length=10) public String status="RECORDED";
    @Column(name="failure_details",nullable=false,length=500) public String failureDetails="";
}
