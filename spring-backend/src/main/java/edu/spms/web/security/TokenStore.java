package edu.spms.web.security;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenStore {
    private record Entry(SpmsPrincipal principal,Instant expiresAt) { }
    private final Map<String,Entry> tokens=new ConcurrentHashMap<>();
    private final SecureRandom random=new SecureRandom();
    public String issue(SpmsPrincipal principal){byte[] bytes=new byte[32];random.nextBytes(bytes);String token=Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);tokens.put(token,new Entry(principal,Instant.now().plusSeconds(43_200)));return token;}
    public SpmsPrincipal resolve(String token){Entry e=tokens.get(token);if(e==null)return null;if(e.expiresAt().isBefore(Instant.now())){tokens.remove(token);return null;}return e.principal();}
    public void revoke(String token){if(token!=null)tokens.remove(token);}
    public void revokeUser(Integer userId){tokens.entrySet().removeIf(e->e.getValue().principal().id().equals(userId));}
}
