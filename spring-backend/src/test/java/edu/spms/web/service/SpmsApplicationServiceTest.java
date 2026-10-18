package edu.spms.web.service;

import edu.spms.web.entity.*;
import edu.spms.web.repository.*;
import edu.spms.web.security.SpmsPrincipal;
import edu.spms.web.security.LegacyAwarePasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SpmsApplicationServiceTest {
    DepartmentRepository departments; EmployeeRepository employees; UserRepository users; AttendanceRepository attendance;
    LeaveRepository leaves; PayrollRepository payroll; PerformanceRepository performance; ReportRepository reports; PayrollPaymentRepository payments;
    SpmsApplicationService service;
    @BeforeEach void setUp(){departments=mock(DepartmentRepository.class);employees=mock(EmployeeRepository.class);users=mock(UserRepository.class);attendance=mock(AttendanceRepository.class);leaves=mock(LeaveRepository.class);payroll=mock(PayrollRepository.class);performance=mock(PerformanceRepository.class);reports=mock(ReportRepository.class);payments=mock(PayrollPaymentRepository.class);service=new SpmsApplicationService(departments,employees,users,attendance,leaves,payroll,performance,reports,payments,new LegacyAwarePasswordEncoder());}
    @Test void employeeDirectoryIsScopedToTheSignedInEmployee(){EmployeeEntity self=employee("SP001"),other=employee("SP002");when(employees.findAll()).thenReturn(List.of(self,other));List<Map<String,Object>> rows=service.list("employees","",employeeActor("SP001"));assertEquals(1,rows.size());assertEquals("SP001",rows.get(0).get("Employee ID"));}
    @Test void employeeLeaveApplicationCannotChooseAnotherEmployee(){EmployeeEntity self=employee("SP001");when(employees.findById("SP001")).thenReturn(Optional.of(self));Map<String,Object> form=Map.of("employeeId","SP002","from","2026-11-03","to","2026-11-04","type","Annual","reason","Family visit");service.applyLeave(form,employeeActor("SP001"));verify(leaves).save(argThat(l->l.employee.id.equals("SP001")&&l.status.equals("PENDING")));}
    @Test void leaveReviewerMustHaveAStaffRole(){assertThrows(AccessDeniedException.class,()->service.decideLeave(1,Map.of("approve",true),employeeActor("SP001")));verifyNoInteractions(leaves);}
    @Test void attendanceCanBeMarkedForTheSignedInEmployee(){EmployeeEntity self=employee("SP001");when(employees.findById("SP001")).thenReturn(Optional.of(self));when(attendance.findAll()).thenReturn(List.of());service.markAttendance(Map.of("employeeId","SP002","workDate","2026-10-10","status","PRESENT"),employeeActor("SP001"));verify(attendance).save(argThat(r->r.employee.id.equals("SP001")&&r.workDate.equals(LocalDate.of(2026,10,10))&&r.status.equals("PRESENT")));}
    @Test void payrollUsesBasicPlusAllowancesMinusDeductions(){EmployeeEntity employee=employee("SP001");employee.salary=new BigDecimal("95000.00");when(employees.findById("SP001")).thenReturn(Optional.of(employee));service.createPayroll(Map.of("employeeId","SP001","month","2026-10","allowances","5000","deductions","2500"),new SpmsPrincipal(2,"hr","HR_MANAGER",null,false));verify(payroll).save(argThat(p->p.netSalary.compareTo(new BigDecimal("97500.00"))==0&&p.payMonth.equals(LocalDate.of(2026,10,1))));}
    @Test void performanceEvaluationRecordsItsEvaluator(){EmployeeEntity employee=employee("SP001");UserEntity evaluator=new UserEntity();evaluator.id=2;evaluator.username="hr";when(employees.findById("SP001")).thenReturn(Optional.of(employee));when(users.findById(2)).thenReturn(Optional.of(evaluator));service.addPerformance(Map.of("employeeId","SP001","period","2026 H2","rating","4.5","remarks","Strong delivery"),new SpmsPrincipal(2,"hr","HR_MANAGER",null,false));verify(performance).save(argThat(p->p.employee.id.equals("SP001")&&p.evaluator.id.equals(2)&&p.rating.compareTo(new BigDecimal("4.5"))==0));}
    @Test void reportGenerationRecordsTheRequestingUser(){EmployeeEntity employee=employee("SP001");UserEntity requester=new UserEntity();requester.id=2;requester.username="hr";when(employees.findAll()).thenReturn(List.of(employee));when(users.findById(2)).thenReturn(Optional.of(requester));Map<String,Object> result=service.report("employees",Map.of("filters","SP001"),new SpmsPrincipal(2,"hr","HR_MANAGER",null,false));assertEquals(1,result.get("count"));verify(reports).save(argThat(r->r.reportType.equals("EMPLOYEE")&&r.requestedBy.id.equals(2)&&r.filterSummary.equals("SP001")));}
    @Test void departmentAdministrationIsAdminOnly(){assertThrows(AccessDeniedException.class,()->service.addDepartment(Map.of("name","Design"),new SpmsPrincipal(9,"hr","HR_MANAGER",null,false)));verifyNoInteractions(departments);}
    private static EmployeeEntity employee(String id){EmployeeEntity e=new EmployeeEntity();e.id=id;e.name=id;e.email=id.toLowerCase()+"@example.com";e.phone="1234567890";e.designation="Engineer";e.joiningDate=LocalDate.of(2024,1,1);e.salary=BigDecimal.valueOf(50000);e.employmentStatus="ACTIVE";return e;}
    private static SpmsPrincipal employeeActor(String employee){return new SpmsPrincipal(1,"employee","EMPLOYEE",employee,false);}
}
