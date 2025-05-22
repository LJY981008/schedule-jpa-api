package com.example.schedulejpaapi.config.authresolver;

import com.example.schedulejpaapi.constant.Const;
import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.exceptions.custom.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@Component
public class MemberAuthArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasMemberAuthAnnotation = parameter.hasParameterAnnotation(MemberAuth.class);
        boolean hasMemberAuthDtoType = Member.class.isAssignableFrom(parameter.getParameterType());
        return hasMemberAuthDtoType && hasMemberAuthAnnotation;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest ServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        Optional<HttpSession> foundSession = Optional.ofNullable(ServletRequest.getSession(false));

        if(foundSession.isEmpty()){
            throw new UnauthorizedException("Unauthorized");
        }

        return foundSession.get().getAttribute(Const.LOGIN_SESSION_KEY);
    }
}
