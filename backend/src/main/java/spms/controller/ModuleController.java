package spms.controller;

import spms.model.User;
import spms.service.SpmsService;
import java.util.*;

public final class ModuleController {
    private final SpmsService service;
    public ModuleController(User actor){service=new SpmsService(actor);}
    public List<Map<String,Object>> load(String view,String query){return service.list(view,query);}
    public Map<String,String> dashboard(){return service.dashboard();}
    public void addEmployee(String id,String n,String e,String p,String d,String title,String date,String salary){service.addEmployee(id,n,e,p,d,title,date,salary);}
    public void updateEmployee(String id,String n,String e,String p,String d,String title,String salary){service.updateEmployee(id,n,e,p,d,title,salary);}
    public void deleteEmployee(String id){service.deleteEmployee(id);}
    public void attendance(String id,String date,String status){service.markAttendance(id,date,status);}
    public void applyLeave(String id,String from,String to,String type,String reason){service.applyLeave(id,from,to,type,reason);}
    public void decideLeave(String id,boolean yes,String note){service.decideLeave(id,yes,note);}
    public void payroll(String id,String month,String allowances,String deductions){service.generatePayroll(id,month,allowances,deductions);}
    public void markPaid(String id){service.markPaid(id);}
    public void addPerformance(String id,String period,String rating,String remarks){service.addPerformance(id,period,rating,remarks);}
    public void updatePerformance(String id,String period,String rating,String remarks){service.updatePerformance(id,period,rating,remarks);}
    public void department(String name,String description){service.addDepartment(name,description);}
    public void updateDepartment(String id,String name,String description){service.updateDepartment(id,name,description);}
    public void deleteDepartment(String id){service.deleteDepartment(id);}
    public void addUser(String name,char[] password,String role,String employee){service.addUser(name,password,role,employee);}
    public void changeRole(String id,String role){service.changeRole(id,role);}
    public void toggleUser(String id,boolean active){service.toggleUser(id,active);}
    public void recordReport(String type,String filters){service.recordReport(type,filters);}
}
