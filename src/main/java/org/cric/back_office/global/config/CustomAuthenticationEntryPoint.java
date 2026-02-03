package org.cric.back_office.global.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증되지 않은 요청에 대한 처리
 * - API 요청: 401 JSON 응답
 * - 페이지 요청: error.html로 리다이렉트
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        String requestUri = request.getRequestURI();

        // API 요청인 경우 JSON 응답
        if (requestUri.startsWith("/api/")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"status\": 401, \"message\": \"인증이 필요합니다. 로그인해주세요.\"}");
        } else {
            // 페이지 요청인 경우 error.html로 리다이렉트
            response.sendRedirect("/global/error.html?error=unauthorized&message=인증이 필요합니다");
        }
    }
}
