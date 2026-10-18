package spms.service;

import spms.dao.ModuleDAO;
import spms.model.SalaryCalculator;
import spms.model.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

/** Application use cases; role checks and validation live outside Swing views. */
public final class SpmsService {
    private final User actor; private final ModuleDAO dao=new ModuleDAO();
    public SpmsService(User actor){this.actor=Objects.requireNonNull(actor);}
    private void require(String module){if(!actor.canAccess(module))throw new SecurityException("Your account is not authorized for this module.");}
    private void staff(String module){require(module);if(actor.role().equals("EMPLOYEE"))throw new SecurityException("Employee accounts can view their own records but cannot manage this workflow.");}
    private void admin(){if(!actor.role().equals("ADMIN"))throw new SecurityException("Only an Admin can manage system administration.");}
    private String module(String view){return switch(view){case "employees"->"Employee Management";case "attendance","leaves"->"Attendance & Leave";case "payroll"->"Payroll Management";case "performance","reports"->"Performance & Reports";case "departments","users"->"Administration";default->throw new IllegalArgumentException("Unknown SPMS view.");};}
    public List<Map<String,Object>> list(String view,String search){require(module(view));return dao.list(view,search,actor.role().equals("EMPLOYEE")?employeeScope():null);}
    public Map<String,String> dashboard(){require("Dashboard");return dao.dashboard(actor.role().equals("EMPLOYEE")?employeeScope():null);}
    public void addEmployee(String id,String name,String email,String phone,String dept,String title,String join,String salary){staff("Employee Management");required(id,name,phone,dept,title);email(email);phone(phone);date(join);dao.addEmployee(id.trim(),name.trim(),email.trim(),phone.trim(),dept.trim(),title.trim(),join,new BigDecimal(amount(salary)));}
    public void updateEmployee(String id,String name,String email,String phone,String dept,String title,String salary){staff("Employee Management");required(name,phone,dept,title);email(email);phone(phone);dao.updateEmployee(id,name,email,phone,dept,title,new BigDecimal(amount(salary)));}
    public void deleteEmployee(String id){staff("Employee Management");dao.deleteEmployee(id);}
    public void markAttendance(String id,String date,String status){require("Attendance & Leave");date(date);if(!Set.of("PRESENT","ABSENT","LEAVE").contains(status))throw new IllegalArgumentException("Choose a valid attendance status.");dao.attendance(scope(id),date,status);}
    public void applyLeave(String id,String from,String to,String type,String reason){require("Attendance & Leave");date(from);date(to);if(LocalDate.parse(to).isBefore(LocalDate.parse(from)))throw new IllegalArgumentException("Leave end date must be on or after its start date.");required(type,reason);dao.applyLeave(scope(id),from,to,type.trim(),reason.trim());}
    public void decideLeave(String id,boolean approve,String note){staff("Attendance & Leave");dao.decideLeave(positiveId(id,"leave request"),approve,actor.id(),note==null?"":note.trim());}
    public void generatePayroll(String employee,String month,String allowance,String deduction){staff("Payroll Management");try{YearMonth.parse(month);}catch(Exception e){throw new IllegalArgumentException("Enter a valid payroll month as YYYY-MM.");}BigDecimal a=new BigDecimal(amount(allowance)),d=new BigDecimal(amount(deduction));List<Map<String,Object>> rows=dao.list("employees",employee,null);BigDecimal basic=rows.stream().filter(r->employee.equals(String.valueOf(r.get("Employee ID")))).map(r->new BigDecimal(String.valueOf(r.get("Salary")))).findFirst().orElseThrow(()->new IllegalArgumentException("Employee was not found."));SalaryCalculator.net(basic,a,d);dao.payroll(employee,month,a,d);}
    public void markPaid(String id){staff("Payroll Management");dao.markPaid(positiveId(id,"payroll record"));}
    public void addPerformance(String employee,String period,String rating,String remarks){staff("Performance & Reports");required(employee,period,remarks);dao.performance(employee,period,ratingValue(rating),remarks,actor.id());}
    public void updatePerformance(String id,String period,String rating,String remarks){staff("Performance & Reports");required(period,remarks);dao.updatePerformance(positiveId(id,"evaluation"),period,ratingValue(rating),remarks);}
    public void addDepartment(String name,String description){admin();required(name);dao.addDepartment(name.trim(),description==null?"":description.trim());}
    public void updateDepartment(String id,String name,String description){admin();required(name);dao.updateDepartment((int)positiveId(id,"department"),name.trim(),description==null?"":description.trim());}
    public void deleteDepartment(String id){admin();dao.deleteDepartment((int)positiveId(id,"department"));}
    public void addUser(String name,char[] password,String role,String employee){admin();try{if(name==null||name.isBlank()||password==null||password.length<8)throw new IllegalArgumentException("Provide a username and a password with at least 8 characters.");if(!Set.of("ADMIN","HR_MANAGER","EMPLOYEE").contains(role))throw new IllegalArgumentException("Choose a valid role.");if(role.equals("EMPLOYEE")&&(employee==null||employee.isBlank()))throw new IllegalArgumentException("Link the employee account to an employee ID.");dao.addUser(name.trim(),password,role,employee);}finally{if(password!=null)Arrays.fill(password,'\0');}}
    public void changeRole(String id,String role){admin();if(!Set.of("ADMIN","HR_MANAGER","EMPLOYEE").contains(role))throw new IllegalArgumentException("Choose a valid account role.");dao.updateUserRole(Integer.parseInt(id),role,actor.id());}
    public void toggleUser(String id,boolean active){admin();dao.toggleUser(Integer.parseInt(id),active,actor.id());}
    public void recordReport(String type,String filter){require("Performance & Reports");dao.recordReport(type,actor.id(),filter);}
    private String scope(String id){if(actor.role().equals("EMPLOYEE"))return employeeScope();return id;}
    private String employeeScope(){if(actor.employeeId()==null||actor.employeeId().isBlank())throw new SecurityException("This account is not linked to an employee record.");return actor.employeeId();}
    private static void required(String... values){for(String v:values)if(v==null||v.isBlank())throw new IllegalArgumentException("Complete all required fields.");}
    private static void email(String v){if(v==null||!v.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))throw new IllegalArgumentException("Enter a valid email address.");}
    private static void phone(String v){if(v==null||!v.matches("[+0-9() -]{7,24}"))throw new IllegalArgumentException("Enter a valid phone number.");}
    private static void date(String v){try{LocalDate.parse(v);}catch(Exception e){throw new IllegalArgumentException("Enter a date as YYYY-MM-DD.");}}
    private static String amount(String s){try{BigDecimal n=new BigDecimal(s);if(n.signum()<0)throw new NumberFormatException();return n.toPlainString();}catch(Exception e){throw new IllegalArgumentException("Enter a valid non-negative amount.");}}
    private static BigDecimal ratingValue(String value){try{BigDecimal n=new BigDecimal(value);if(n.compareTo(BigDecimal.ONE)<0||n.compareTo(BigDecimal.valueOf(5))>0)throw new NumberFormatException();return n;}catch(Exception e){throw new IllegalArgumentException("Rating must be a number between 1 and 5.");}}
    private static long positiveId(String value,String label){try{long id=Long.parseLong(value);if(id>0)return id;}catch(Exception ignored){}throw new IllegalArgumentException("Choose a valid "+label+" record.");}
}
