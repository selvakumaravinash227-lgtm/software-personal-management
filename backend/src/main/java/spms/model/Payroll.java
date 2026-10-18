package spms.model;
import java.math.BigDecimal;import java.time.LocalDate;
public record Payroll(long id,String employeeId,LocalDate month,BigDecimal basic,BigDecimal allowances,BigDecimal deductions,Status status,LocalDate paidOn){public enum Status{DRAFT,PROCESSED,PAID}public BigDecimal net(){return SalaryCalculator.net(basic,allowances,deductions);}}
