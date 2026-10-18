package edu.spms.web.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="reports")
public class ReportEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="report_id") public Long id;
    @Column(name="report_type",nullable=false,length=20) public String reportType;
    @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="requested_by",nullable=false) public UserEntity requestedBy;
    @Column(name="generated_at",insertable=false,updatable=false) public LocalDateTime generatedAt;
    @Column(name="filter_summary",nullable=false,length=500) public String filterSummary="";
}
