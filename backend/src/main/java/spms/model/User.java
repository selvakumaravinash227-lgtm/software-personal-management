package spms.model;

/** Authenticated user abstraction; role subclasses implement module access polymorphically. */
public abstract class User {
    private final int id;private final String username,role,employeeId;
    protected User(int id,String username,String role,String employeeId){this.id=id;this.username=username;this.role=role;this.employeeId=employeeId;}
    public int id(){return id;}public String username(){return username;}public String role(){return role;}public String employeeId(){return employeeId;}
    public abstract boolean canAccess(String module);
}
