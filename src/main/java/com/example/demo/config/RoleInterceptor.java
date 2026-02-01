package com.example.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    private static final String ROLE_HEADER = "X-Role";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String roleHeader = request.getHeader(ROLE_HEADER);

        if (roleHeader == null || roleHeader.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing X-Role header");
            return false;
        }

        Role role = Role.fromString(roleHeader);
        if (role == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid role value");
            return false;
        }

        RoleContext.setRole(role);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        RoleContext.clear();
    }
}
