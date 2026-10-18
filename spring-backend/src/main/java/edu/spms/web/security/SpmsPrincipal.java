package edu.spms.web.security;

public record SpmsPrincipal(Integer id,String username,String role,String employeeId,boolean mustChangePassword) { }
