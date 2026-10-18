package spms.model;
import java.util.Set;
public final class Employee extends User {private static final Set<String> MODULES=Set.of("Dashboard","User Authentication / Profile","Employee Management","Attendance & Leave","Payroll Management","Performance & Reports");public Employee(int id,String name,String role,String employeeId){super(id,name,role,employeeId);}@Override public boolean canAccess(String module){return MODULES.contains(module);}}
