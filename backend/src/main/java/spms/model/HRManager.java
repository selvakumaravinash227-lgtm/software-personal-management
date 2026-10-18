package spms.model;
public final class HRManager extends User {public HRManager(int id,String name,String role,String employeeId){super(id,name,role,employeeId);}@Override public boolean canAccess(String module){return !module.equals("Administration");}}
