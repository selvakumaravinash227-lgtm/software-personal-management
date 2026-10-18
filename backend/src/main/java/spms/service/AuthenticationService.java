package spms.service;

import spms.dao.UserDAO;
import spms.model.User;

public final class AuthenticationService {
    private final UserDAO users=new UserDAO();
    public User login(String username,char[] password){
        if(username==null||username.isBlank()||password==null||password.length==0)throw new IllegalArgumentException("Enter your username and password.");
        return users.authenticate(username.trim(),password);
    }
}
