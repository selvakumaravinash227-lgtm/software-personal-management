package spms.model;
import java.math.BigDecimal;import java.time.LocalDate;
public record Performance(long id,String employeeId,String period,BigDecimal rating,String remarks,LocalDate evaluatedOn) {}
