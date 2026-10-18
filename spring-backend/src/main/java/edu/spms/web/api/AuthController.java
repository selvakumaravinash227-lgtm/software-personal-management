package edu.spms.web.api;

import edu.spms.web.entity.UserEntity;
import edu.spms.web.repository.UserRepository;
import edu.spms.web.security.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authentication; private final UserRepository users; private final TokenStore tokens; private final PasswordEncoder encoder;
    public AuthController(AuthenticationManager authentication,UserRepository users,TokenStore tokens,PasswordEncoder encoder){this.authentication=authentication;this.users=users;this.tokens=tokens;this.encoder=encoder;}
    @PostMapping("/login") @Transactional public Map<String,Object> login(@RequestBody Map<String,Object> body){
        String username=String.valueOf(body.getOrDefault("username","")),password=String.valueOf(body.getOrDefault("password",""));
        try{authentication.authenticate(new UsernamePasswordAuthenticationToken(username,password));}catch(AuthenticationException ex){if(hasDataAccessCause(ex))throw new org.springframework.dao.DataAccessResourceFailureException("SPMS database authentication lookup failed",ex);throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED,"Username or password is incorrect.");}
        UserEntity account=users.findByUsernameIgnoreCaseAndActiveTrue(username).orElseThrow();if(account.passwordHash.startsWith("pbkdf2$"))account.passwordHash=encoder.encode(password);
        SpmsPrincipal profile=principal(account);return Map.of("token",tokens.issue(profile),"user",profile(profile));
    }
    @PostMapping("/password") @Transactional public Map<String,Object> changePassword(@RequestBody Map<String,Object> body,Authentication auth,@RequestHeader(value="Authorization",required=false)String authorization){
        SpmsPrincipal current=(SpmsPrincipal)auth.getPrincipal();UserEntity account=users.findById(current.id()).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED,"Sign in again to continue."));
        String oldPassword=String.valueOf(body.getOrDefault("currentPassword","")),newPassword=String.valueOf(body.getOrDefault("newPassword",""));
        if(oldPassword.isBlank()||!encoder.matches(oldPassword,account.passwordHash))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"Current password is incorrect.");
        if(newPassword.length()<12)throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"Choose a password with at least 12 characters.");
        if(encoder.matches(newPassword,account.passwordHash))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"Choose a password you have not used for this account.");
        account.passwordHash=encoder.encode(newPassword);account.mustChangePassword=false;SpmsPrincipal updated=principal(account);tokens.revoke(token(authorization));return Map.of("token",tokens.issue(updated),"user",profile(updated));
    }
    @PostMapping("/logout") @org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT) public void logout(@RequestHeader(value="Authorization",required=false)String auth){tokens.revoke(token(auth));}
    @GetMapping("/me") public Map<String,Object> me(org.springframework.security.core.Authentication auth){return profile((SpmsPrincipal)auth.getPrincipal());}
    private static SpmsPrincipal principal(UserEntity u){return new SpmsPrincipal(u.id,u.username,u.role,u.employee==null?null:u.employee.id,u.mustChangePassword);}
    private static Map<String,Object> profile(SpmsPrincipal p){Map<String,Object> m=new LinkedHashMap<>();m.put("id",p.id());m.put("username",p.username());m.put("role",p.role());m.put("employeeId",p.employeeId());m.put("mustChangePassword",p.mustChangePassword());return m;}
    private static String token(String h){return h!=null&&h.startsWith("Bearer ")?h.substring(7):null;}
    private static boolean hasDataAccessCause(Throwable error){for(Throwable c=error;c!=null;c=c.getCause())if(c instanceof org.springframework.dao.DataAccessException)return true;return false;}
}
