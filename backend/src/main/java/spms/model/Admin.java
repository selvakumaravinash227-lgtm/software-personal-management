package spms.model;
public final class Admin extends User {public Admin(int id,String name,String role,String employeeId){super(id,name,role,employeeId);}@Override public boolean canAccess(String module){return true;}}
