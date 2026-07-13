package ca.sait.aris.lims.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Thread-level user context container
 * Responsible for securely passing the identity information of the current operator throughout the lifecycle of the current HTTP request.
 * Warning: The `clear()` method must be called in the `finally` block of the Filter to prevent memory leaks or privilege escalation caused by Tomcat thread pool reuse!
 */
public class UserContext {

    // Use ThreadLocal to bind a Map to store multiple context variables of the current thread.
    private static final ThreadLocal<Map<String, Object>> USER_HOLDER = ThreadLocal.withInitial(HashMap::new);

    /**
     * Injects the current user's ID
     */
    public static void setUserId(String userId) {
        USER_HOLDER.get().put("userId", userId);
    }

    /**
     * Gets the current user's ID
     */
    public static String getUserId() {
        return (String) USER_HOLDER.get().get("userId");
    }

    /**
     * Injects the current user's Role
     */
    public static void setRole(String role) {
        USER_HOLDER.get().put("role", role);
    }

    /**
     * Gets the current user's Role
     */
    public static String getRole() {
        return (String) USER_HOLDER.get().get("role");
    }

    /**
     * Injects the current user's Email
     */
    public static void setEmail(String email) {
        USER_HOLDER.get().put("email", email);
    }

    /**
     * Gets the current user's Email
     */
    public static String getEmail() {
        return (String) USER_HOLDER.get().get("email");
    }

    /**
     * 🚨 Core defense: Clear the context data of the current thread
     * Must be forcibly called at the end of the request (such as in the finally block of a Filter).
     */
    public static void clear() {
        USER_HOLDER.remove();
    }
}