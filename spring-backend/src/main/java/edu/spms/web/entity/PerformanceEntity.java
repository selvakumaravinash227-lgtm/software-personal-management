package edu.spms.web.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name="performance")
public class PerformanceEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="performance_id") public Long id;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="employee_id",nullable=false) public EmployeeEntity employee;
    @Column(name="evaluation_period",nullable=false,length=30) public String period;
    @Column(nullable=false,precision=3,scale=1) public BigDecimal rating;
    @Column(nullable=false,length=1000) public String remarks;
    @Column(name="technical_skills_score",precision=3,scale=1) public BigDecimal technicalSkillsScore;
    @Column(name="communication_score",precision=3,scale=1) public BigDecimal communicationScore;
    @Column(name="teamwork_score",precision=3,scale=1) public BigDecimal teamworkScore;
    @Column(name="productivity_score",precision=3,scale=1) public BigDecimal productivityScore;
    @Column(name="quality_score",precision=3,scale=1) public BigDecimal qualityScore;
    @Column(name="improvement_suggestions",nullable=false,length=1000) public String improvementSuggestions="";
    @Column(name="future_goals",nullable=false,length=1000) public String futureGoals="";
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="evaluator_id") public UserEntity evaluator;
    @Column(name="evaluated_at",nullable=false) public LocalDate evaluatedAt;
    @Column(name="updated_at",insertable=false,updatable=false) public java.time.LocalDateTime updatedAt;
}
