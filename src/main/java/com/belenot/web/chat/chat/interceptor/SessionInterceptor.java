package com.belenot.web.chat.chat.interceptor;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

public class SessionInterceptor implements HandlerInterceptor {

    private String[] blocked = new String[]{"/chat", "/logout", "/client"};
    private String[] nonBlocked = new String[]{"/registration", "/authorization"};
    private String blockedRedirect = "/authorization";
    private String nonBlockedRedirect = "/chat";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();
        HttpSession session = request.getSession(false);
        if (Arrays.stream(blocked).anyMatch( s -> s.length() <= uri.length() && uri.subSequence(0, s.length()).equals(s))) {
            if (session == null || session.getAttribute("client") == null) {
                response.sendRedirect(blockedRedirect);
                return false;
            }
        }
        if (Arrays.stream(nonBlocked).anyMatch( s -> s.length() <= uri.length() && uri.subSequence(0, s.length()).equals(s))) {
            if (session != null && session.getAttribute("client") != null) {
                response.sendRedirect(nonBlockedRedirect);
                return false;
            }
        }
        return true;
    }
    
}