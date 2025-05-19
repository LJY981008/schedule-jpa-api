package com.example.schedulejpaapi.filter;

import com.example.schedulejpaapi.constant.Const;
import com.example.schedulejpaapi.exceptions.custom.SessionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

// 로그인 확인 필터
public class LoginFilter implements Filter {

    private static final String[] NEED_LOGOUT_LIST = {"/member/signup", "/member/login"};
    private static final String[] NEED_LOGIN_LIST = {
            "/member/logout", "/member/update",
            "/member/remove", "/post/create",
            "/post/change", "/post/remove",
            "/comment/create", "/comment/change",
    };

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();

        Optional<Object> session = Optional.ofNullable(request.getSession().getAttribute(Const.LOGIN_SESSION_KEY));

        try {
            if (isNeedLogin(requestURI)) {
                if (session.isEmpty()) {
                    throw new SessionException("Unauthorized");
                }
            } else if (isNeedLogout(requestURI)) {
                if (session.isPresent()) {
                    throw new SessionException("Already Logout");
                }
            }

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (SessionException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Map<String, String> error = Map.of("error", e.getMessage());
            String json = new ObjectMapper().writeValueAsString(error);
            response.getWriter().write(json);
        }
    }

    // 로그인이 필요한 URI
    private boolean isNeedLogin(String requestURI) {
        return PatternMatchUtils.simpleMatch(NEED_LOGIN_LIST, requestURI);
    }

    // 로그아웃이 필요한 URI
    private boolean isNeedLogout(String requestURI) {
        return PatternMatchUtils.simpleMatch(NEED_LOGOUT_LIST, requestURI);
    }
}
