package spms.model;
import java.math.BigDecimal;import java.time.LocalDate;
public record EmployeeRecord(String id,String name,String email,String phone,Department department,String designation,LocalDate joiningDate,BigDecimal salary,String status) {}
