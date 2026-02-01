package com.example.demo.config;

public class RoleContext {

    private static final ThreadLocal<Role> currentRole = new ThreadLocal<>();

    public static void setRole(Role role) {
        currentRole.set(role);
    }

    public static Role getRole() {
        return currentRole.get();
    }

    public static void clear() {
        currentRole.remove();
    }
}
