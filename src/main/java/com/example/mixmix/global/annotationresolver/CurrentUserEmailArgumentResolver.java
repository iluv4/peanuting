package com.example.mixmix.global.annotationresolver;

import com.example.mixmix.auth.api.dto.request.TokenReqDto;
import com.example.mixmix.global.annotation.CurrentUserEmail;
import com.example.mixmix.global.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentUserEmailArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenProvider tokenProvider;

    public CurrentUserEmailArgumentResolver(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUserEmail.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            TokenReqDto tokenReqDto = new TokenReqDto(token);

            return tokenProvider.getUserEmailFromToken(tokenReqDto);
        }

        return null;
    }
}