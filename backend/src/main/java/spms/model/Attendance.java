package spms.model;
import java.time.LocalDate;import java.time.LocalTime;
public record Attendance(long id,String employeeId,LocalDate date,Status status,LocalTime checkIn,LocalTime checkOut){public enum Status{PRESENT,ABSENT,LEAVE}}
