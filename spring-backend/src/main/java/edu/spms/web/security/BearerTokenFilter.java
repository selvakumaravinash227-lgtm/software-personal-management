package edu.spms.web.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

public class BearerTokenFilter extends OncePerRequestFilter {
    private final TokenStore tokens;
    public BearerTokenFilter(TokenStore tokens){this.tokens=tokens;}
    @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain)throws ServletException,IOException{
        String header=request.getHeader("Authorization");
        if(header!=null&&header.startsWith("Bearer ")){
            SpmsPrincipal principal=tokens.resolve(header.substring(7));
            if(principal!=null){
                boolean passwordChangeRequest="POST".equalsIgnoreCase(request.getMethod())&&request.getRequestURI().endsWith("/api/auth/password");
                boolean logoutRequest="POST".equalsIgnoreCase(request.getMethod())&&request.getRequestURI().endsWith("/api/auth/logout");
                if(principal.mustChangePassword()&&!passwordChangeRequest&&!logoutRequest){response.setStatus(HttpServletResponse.SC_FORBIDDEN);response.setContentType("application/json");response.getWriter().write("{\"message\":\"Change your temporary password before using SPMS.\"}");return;}
                var auth=new UsernamePasswordAuthenticationToken(principal,null,List.of(new SimpleGrantedAuthority("ROLE_"+principal.role())));SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request,response);
    }
}
