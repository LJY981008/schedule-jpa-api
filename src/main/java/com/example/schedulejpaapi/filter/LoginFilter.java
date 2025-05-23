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

/**
 * 로그인 상태에 따라 접근을 제어하는 필터
 * 특정 URI에 로그인이 필요한지 로그아웃이 필요한지 필터링
 */
public class LoginFilter implements Filter {

    private static final String[] NEED_LOGOUT_LIST = {"/member/signup", "/member/login"};
    private static final String[] NEED_LOGIN_LIST = {
            "/member/logout", "/member/update", "/member/remove",
            "/post/create", "/post/change", "/post/remove",
            "/comment/create", "/comment/change",
    };

    /**
     * HTTP 요청을 확인해 접근제어
     *
     * @param servletRequest  요청 객체
     * @param servletResponse 응답 객체
     * @param filterChain     다음 필터로 요청을 전달하는 객체
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
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

    /**
     * 요청 URI가 로그인이 필요한지 체크
     *
     * @param requestURI 요청 URI
     * @return 로그인이 필요하면 {@code true} 필요 없으면 {@code false}
     */
    private boolean isNeedLogin(String requestURI) {
        return PatternMatchUtils.simpleMatch(NEED_LOGIN_LIST, requestURI);
    }

    /**
     * 요청 URI가 로그아웃이 필요한지 체크
     *
     * @param requestURI 요청 URI
     * @return 로그아웃이 필요하면 {@code true} 필요 없으면 {@code false}
     */
    private boolean isNeedLogout(String requestURI) {
        return PatternMatchUtils.simpleMatch(NEED_LOGOUT_LIST, requestURI);
    }
}
