package spms.dao;

import spms.util.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/** JDBC persistence for the workforce modules. All user data is passed as prepared parameters. */
public final class ModuleDAO {
    private static final Logger LOGGER=Logger.getLogger(ModuleDAO.class.getName());
    public List<Map<String,Object>> list(String view,String search,String employeeId){
        String sql=switch(view){
            case "employees"->"SELECT e.employee_id AS `Employee ID`,e.name AS Name,d.name AS Department,e.designation AS Designation,e.email AS Email,e.phone AS Phone,e.joining_date AS `Joining Date`,e.employment_status AS Status,e.salary AS Salary FROM employees e LEFT JOIN departments d USING(department_id) WHERE (?='%%' OR e.employee_id LIKE ? OR e.name LIKE ? OR e.email LIKE ? OR d.name LIKE ?)"+(employeeId==null?"":" AND e.employee_id=?")+" ORDER BY e.name";
            case "attendance"->"SELECT a.attendance_id AS ID,a.work_date AS Date,a.employee_id AS `Employee ID`,e.name AS Employee,a.status AS Status,a.check_in AS `Check In`,a.check_out AS `Check Out` FROM attendance a JOIN employees e USING(employee_id) WHERE (?='%%' OR e.name LIKE ? OR a.employee_id LIKE ?)"+(employeeId==null?"":" AND a.employee_id=?")+" ORDER BY a.work_date DESC";
            case "leaves"->"SELECT l.leave_id AS ID,l.employee_id AS `Employee ID`,e.name AS Employee,l.leave_type AS Type,l.start_date AS FromDate,l.end_date AS ToDate,l.reason AS Reason,l.status AS Status,l.decision_note AS `Decision Note` FROM leave_requests l JOIN employees e USING(employee_id) WHERE (?='%%' OR e.name LIKE ? OR l.employee_id LIKE ? OR l.status LIKE ?)"+(employeeId==null?"":" AND l.employee_id=?")+" ORDER BY l.requested_at DESC";
            case "payroll"->"SELECT p.payroll_id AS ID,p.employee_id AS `Employee ID`,e.name AS Employee,DATE_FORMAT(p.pay_month,'%Y-%m') AS Month,p.basic_salary AS Basic,p.allowances AS Allowances,p.deductions AS Deductions,p.net_salary AS `Net Salary`,p.status AS Status,p.pay_date AS `Payment Date` FROM payroll p JOIN employees e USING(employee_id) WHERE (?='%%' OR e.name LIKE ? OR p.employee_id LIKE ?)"+(employeeId==null?"":" AND p.employee_id=?")+" ORDER BY p.pay_month DESC";
            case "performance"->"SELECT p.performance_id AS ID,p.employee_id AS `Employee ID`,e.name AS Employee,p.evaluation_period AS Period,p.rating AS Rating,COALESCE(u.username,'—') AS Evaluator,p.remarks AS Remarks,p.evaluated_at AS Date FROM performance p JOIN employees e USING(employee_id) LEFT JOIN users u ON u.user_id=p.evaluator_id WHERE (?='%%' OR e.name LIKE ? OR p.employee_id LIKE ? OR p.evaluation_period LIKE ?)"+(employeeId==null?"":" AND p.employee_id=?")+" ORDER BY p.evaluated_at DESC";
            case "departments"->"SELECT department_id AS ID,name AS Department,description AS Description FROM departments WHERE (?='%%' OR name LIKE ? OR description LIKE ?) ORDER BY name";
            case "users"->"SELECT user_id AS ID,username AS Username,role AS Role,COALESCE(employee_id,'—') AS `Employee ID`,active AS Active FROM users WHERE (?='%%' OR username LIKE ? OR role LIKE ?) ORDER BY username";
            case "reports"->"SELECT report_id AS ID,report_type AS Type,generated_at AS Generated,filter_summary AS Filters FROM reports WHERE (?='%%' OR report_type LIKE ? OR filter_summary LIKE ?) ORDER BY generated_at DESC";
            default->throw new IllegalArgumentException("Unknown SPMS data view.");
        };
        try(Connection c=DatabaseConnection.getInstance().open();PreparedStatement p=c.prepareStatement(sql)){
            String q="%"+(search==null?"":search.trim())+"%";int n=switch(view){case "employees"->5;case "attendance","payroll"->3;case "leaves","performance"->4;case "departments","users","reports"->3;default->throw new IllegalArgumentException("Unknown SPMS view.");};
            for(int i=1;i<=n;i++)p.setString(i,q);if(employeeId!=null)p.setString(n+1,employeeId);
            try(ResultSet r=p.executeQuery()){ResultSetMetaData md=r.getMetaData();List<Map<String,Object>> rows=new ArrayList<>();while(r.next()){Map<String,Object> row=new LinkedHashMap<>();for(int i=1;i<=md.getColumnCount();i++)row.put(md.getColumnLabel(i),r.getObject(i));rows.add(row);}return rows;}
        }catch(SQLException e){throw failure("Could not load records.",e);}
    }
    public Map<String,String> dashboard(String employeeId){
        Map<String,String> result=new LinkedHashMap<>();String scope=employeeId==null?"":" AND employee_id=?";
        try(Connection c=DatabaseConnection.getInstance().open()){
            result.put("Total Employees",employeeId==null?scalar(c,"SELECT COUNT(*) FROM employees WHERE employment_status='ACTIVE'",null):"1");
            result.put("Present Today",scalar(c,"SELECT COUNT(*) FROM attendance WHERE work_date=CURDATE() AND status='PRESENT'"+scope,employeeId));
            result.put("On Leave",scalar(c,"SELECT COUNT(*) FROM leave_requests WHERE status='APPROVED' AND CURDATE() BETWEEN start_date AND end_date"+scope,employeeId));
            result.put("Pending Requests",scalar(c,"SELECT COUNT(*) FROM leave_requests WHERE status='PENDING'"+scope,employeeId));
            result.put("Payroll This Month",scalar(c,"SELECT COALESCE(SUM(net_salary),0) FROM payroll WHERE pay_month=DATE_FORMAT(CURDATE(),'%Y-%m-01')"+scope,employeeId));
            result.put("Department Distribution",departmentDistribution(c,employeeId));
            result.put("Recent Activity",recentActivity(c,employeeId));
            return result;
        }catch(SQLException e){throw failure("Could not load dashboard data.",e);}
    }
    private String scalar(Connection c,String sql,String employee)throws SQLException{try(PreparedStatement p=c.prepareStatement(sql)){if(employee!=null)p.setString(1,employee);try(ResultSet r=p.executeQuery()){r.next();return String.valueOf(r.getObject(1));}}}
    private String departmentDistribution(Connection c,String employee)throws SQLException{String sql=employee==null?"SELECT d.name,COUNT(e.employee_id) FROM departments d LEFT JOIN employees e ON e.department_id=d.department_id AND e.employment_status='ACTIVE' GROUP BY d.department_id,d.name ORDER BY d.name":"SELECT d.name,1 FROM employees e JOIN departments d USING(department_id) WHERE e.employee_id=?";StringJoiner result=new StringJoiner("|");try(PreparedStatement p=c.prepareStatement(sql)){if(employee!=null)p.setString(1,employee);try(ResultSet r=p.executeQuery()){while(r.next())result.add(r.getString(1)+"="+r.getInt(2));}}return result.toString();}
    private String recentActivity(Connection c,String employee)throws SQLException{String sql=employee==null?"SELECT activity FROM (SELECT CONCAT('Leave request ',leave_id,' · ',status) activity,requested_at occurred FROM leave_requests UNION ALL SELECT CONCAT(report_type,' report generated') activity,generated_at occurred FROM reports) a ORDER BY occurred DESC LIMIT 5":"SELECT CONCAT('Leave request ',leave_id,' · ',status) FROM leave_requests WHERE employee_id=? ORDER BY requested_at DESC LIMIT 5";StringJoiner result=new StringJoiner("|");try(PreparedStatement p=c.prepareStatement(sql)){if(employee!=null)p.setString(1,employee);try(ResultSet r=p.executeQuery()){while(r.next())result.add(r.getString(1));}}return result.toString();}
    public void addEmployee(String id,String name,String email,String phone,String dept,String title,String join,BigDecimal salary){String sql="INSERT INTO employees(employee_id,name,email,phone,address,department_id,designation,joining_date,salary) SELECT ?,?,?,?, '',department_id,?,?,? FROM departments WHERE name=?";int n=run(sql,id,name,email,phone,title,java.sql.Date.valueOf(join),salary,dept);if(n==0)throw new IllegalArgumentException("Department was not found. Add it in Administration first.");}
    public void updateEmployee(String id,String name,String email,String phone,String dept,String title,BigDecimal salary){String sql="UPDATE employees e JOIN departments d ON d.name=? SET e.name=?,e.email=?,e.phone=?,e.department_id=d.department_id,e.designation=?,e.salary=? WHERE e.employee_id=?";int n=run(sql,dept,name,email,phone,title,salary,id);if(n==0)throw new IllegalArgumentException("Employee or department was not found.");}
    public void deleteEmployee(String id){run("DELETE FROM employees WHERE employee_id=?",id);}
    public void attendance(String id,String date,String status){run("INSERT INTO attendance(employee_id,work_date,status) VALUES(?,?,?) ON DUPLICATE KEY UPDATE status=VALUES(status)",id,java.sql.Date.valueOf(date),status);}
    public void applyLeave(String id,String from,String to,String type,String reason){run("INSERT INTO leave_requests(employee_id,start_date,end_date,leave_type,reason) VALUES(?,?,?,?,?)",id,java.sql.Date.valueOf(from),java.sql.Date.valueOf(to),type,reason);}
    public void decideLeave(long id,boolean approve,int reviewer,String note){int n=run("UPDATE leave_requests SET status=?,reviewed_by=?,reviewed_at=NOW(),decision_note=? WHERE leave_id=? AND status='PENDING'",approve?"APPROVED":"REJECTED",reviewer,note,id);if(n==0)throw new IllegalArgumentException("Pending leave request was not found.");}
    public void payroll(String id,String month,BigDecimal allowances,BigDecimal deductions){String sql="INSERT INTO payroll(employee_id,pay_month,basic_salary,allowances,deductions) SELECT employee_id,?,salary,?,? FROM employees WHERE employee_id=?";int n=run(sql,java.sql.Date.valueOf(month+"-01"),allowances,deductions,id);if(n==0)throw new IllegalArgumentException("Employee was not found.");}
    public void markPaid(long id){int n=run("UPDATE payroll SET status='PAID',pay_date=CURDATE() WHERE payroll_id=? AND status<>'PAID'",id);if(n==0)throw new IllegalArgumentException("Payroll record was not found or has already been paid.");}
    public void performance(String employee,String period,BigDecimal rating,String remarks,int evaluator){run("INSERT INTO performance(employee_id,evaluation_period,rating,remarks,evaluator_id,evaluated_at) VALUES(?,?,?,?,?,CURDATE())",employee,period,rating,remarks,evaluator);}
    public void updatePerformance(long id,String period,BigDecimal rating,String remarks){run("UPDATE performance SET evaluation_period=?,rating=?,remarks=? WHERE performance_id=?",period,rating,remarks,id);}
    public void addDepartment(String name,String description){run("INSERT INTO departments(name,description) VALUES(?,?)",name,description);}
    public void updateDepartment(int id,String name,String description){run("UPDATE departments SET name=?,description=? WHERE department_id=?",name,description,id);}
    public void deleteDepartment(int id){run("DELETE FROM departments WHERE department_id=?",id);}
    public void addUser(String username,char[] password,String role,String employee){new UserDAO().create(username,password,role,employee);}
    public void updateUserRole(int id,String role,int actor){if(id==actor)throw new IllegalArgumentException("Your active account role cannot be changed.");run("UPDATE users SET role=? WHERE user_id=?",role,id);}
    public void toggleUser(int id,boolean active,int actor){if(id==actor)throw new IllegalArgumentException("You cannot deactivate your active account.");run("UPDATE users SET active=? WHERE user_id=?",active,id);}
    public void recordReport(String type,int userId,String filter){String mapped=switch(type){case "employees"->"EMPLOYEE";case "attendance"->"ATTENDANCE";case "leaves"->"LEAVE";case "payroll"->"PAYROLL";case "performance"->"PERFORMANCE";default->throw new IllegalArgumentException("Choose a report type.");};run("INSERT INTO reports(report_type,requested_by,filter_summary) VALUES(?,?,?)",mapped,userId,filter==null?"":filter);}
    private int run(String sql,Object... values){try(Connection c=DatabaseConnection.getInstance().open();PreparedStatement p=c.prepareStatement(sql)){for(int i=0;i<values.length;i++)p.setObject(i+1,values[i]);return p.executeUpdate();}catch(SQLException e){throw failure("The database could not complete this operation. Check required fields and related records.",e);}}
    private DataAccessException failure(String message,SQLException e){LOGGER.log(Level.SEVERE,"SPMS database operation failed",e);return new DataAccessException(message,e);}
}
