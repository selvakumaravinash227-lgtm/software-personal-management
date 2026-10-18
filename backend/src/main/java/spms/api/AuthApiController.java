package spms.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import spms.dao.UserDAO;
import spms.model.User;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {
    private record Session(User user, long expiresAt) {}
    private static final Map<String, Session> SESSIONS = new ConcurrentHashMap<>();
    private static final SecureRandom RANDOM = new SecureRandom();
    private final UserDAO users = new UserDAO();

    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody Map<String,Object> body) {
        String username = text(body,"username"), secret = text(body,"password");
        User user = users.authenticate(username, secret.toCharArray());
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Username or password is incorrect.");
        byte[] bytes = new byte[32]; RANDOM.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        SESSIONS.put(token,new Session(user,System.currentTimeMillis()+12*60*60*1000L));
        return Map.of("token",token,"user",profile(user));
    }
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader(value="Authorization",required=false) String authorization) { SESSIONS.remove(token(authorization)); }
    @GetMapping("/me") public Map<String,Object> me(@RequestHeader(value="Authorization",required=false) String authorization) { return profile(actor(authorization)); }

    static User actor(String authorization) {
        Session session=SESSIONS.get(token(authorization));
        if(session==null || session.expiresAt()<System.currentTimeMillis()) { if(session!=null) SESSIONS.values().remove(session); throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Your session has expired. Sign in again."); }
        return session.user();
    }
    static void revoke(int userId) { SESSIONS.entrySet().removeIf(entry -> entry.getValue().user().id()==userId); }
    private static String token(String header){return header!=null&&header.startsWith("Bearer ")?header.substring(7):"";}
    private static Map<String,Object> profile(User user){Map<String,Object> map=new LinkedHashMap<>();map.put("id",user.id());map.put("username",user.username());map.put("role",user.role());map.put("employeeId",user.employeeId());return map;}
    static String text(Map<String,Object> body,String key){Object v=body.get(key);return v==null?"":String.valueOf(v);}
}
