package spms.controller;

import spms.model.User;
import spms.service.AuthenticationService;

public final class AuthenticationController {
    private final AuthenticationService service=new AuthenticationService();
    public User signIn(String username,char[] password){return service.login(username,password);}
}
