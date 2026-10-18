package spms.dao;

import spms.model.*;
import spms.service.PasswordService;
import spms.util.DatabaseConnection;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class UserDAO {
    private static final Logger LOGGER=Logger.getLogger(UserDAO.class.getName());
    public User authenticate(String username,char[] password){
        String sql="SELECT user_id,username,role,employee_id,password_hash FROM users WHERE username=? AND active=TRUE";
        try(Connection c=DatabaseConnection.getInstance().open();PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,username);try(ResultSet r=p.executeQuery()){
                if(!r.next()||!PasswordService.verify(password,r.getString("password_hash")))return null;
                int id=r.getInt("user_id");String name=r.getString("username"),role=r.getString("role"),employee=r.getString("employee_id");
                return switch(role){case "ADMIN"->new Admin(id,name,role,employee);case "HR_MANAGER"->new HRManager(id,name,role,employee);default->new Employee(id,name,role,employee);};
            }
        }catch(SQLException e){LOGGER.log(Level.SEVERE,"SPMS authentication database error",e);throw new DataAccessException("Could not sign in. Check the SPMS_DB connection and setup.",e);}
    }
    public void create(String username,char[] password,String role,String employeeId){
        String sql="INSERT INTO users(username,password_hash,role,employee_id) VALUES(?,?,?,?)";
        try(Connection c=DatabaseConnection.getInstance().open();PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,username);p.setString(2,PasswordService.hash(password));p.setString(3,role);
            if(employeeId==null||employeeId.isBlank())p.setNull(4,Types.VARCHAR);else p.setString(4,employeeId);
            p.executeUpdate();
        }catch(SQLException e){LOGGER.log(Level.SEVERE,"SPMS account creation database error",e);throw new DataAccessException("Could not create account. Check that username and employee are unique.",e);}
    }
}
