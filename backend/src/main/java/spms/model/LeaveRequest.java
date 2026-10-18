package spms.model;
import java.time.LocalDate;
public record LeaveRequest(long id,String employeeId,LocalDate start,LocalDate end,String type,String reason,Status status){public enum Status{PENDING,APPROVED,REJECTED}}
