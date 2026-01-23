package org.cric.back_office.global.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cric.back_office.global.service.TokenService;
import org.cric.back_office.global.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, TokenService tokenService) {
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);
        System.out.println(token);
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                Integer userId = jwtUtil.getUserIdFromToken(token);
                String tokenId = jwtUtil.getTokenIdFromToken(token);

                // Redis에서 토큰 ID 유효성 검증 (중복 로그인 방지)
                if (!tokenService.validateTokenId(userId, tokenId)) {
                    // 다른 기기에서 로그인하여 현재 토큰이 무효화됨
                    handleAuthenticationError(request, response, "다른 기기에서 로그인하여 현재 세션이 종료되었습니다.", "session_expired");
                    return;
                }

                String email = jwtUtil.getEmailFromToken(token);
                String name = jwtUtil.getNameFromToken(token);
                String role = jwtUtil.getRoleFromToken(token);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        name, // principal로 name 저장 (수정 시 사용)
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // 토큰이 유효하지 않음 (만료 포함)
                handleAuthenticationError(request, response, "토큰이 만료되었거나 유효하지 않습니다.", "token_invalid");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 토큰 추출 (Authorization 헤더 우선, 없으면 쿠키에서)
     */
    private String extractToken(HttpServletRequest request) {
        // 1. Authorization 헤더에서 토큰 추출
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        // 2. 쿠키에서 토큰 추출
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    /**
     * 인증 에러 처리
     * - API 요청: JSON 응답
     * - 페이지 요청: error.html로 리다이렉트
     */
    private void handleAuthenticationError(HttpServletRequest request, HttpServletResponse response,
            String message, String errorCode) throws IOException {
        String requestUri = request.getRequestURI();

        if (requestUri.startsWith("/api/")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter()
                    .write("{\"status\": 401, \"error\": \"" + errorCode + "\", \"message\": \"" + message + "\"}");
        } else {
            response.sendRedirect("/example/error.html?error=" + errorCode + "&message="
                    + java.net.URLEncoder.encode(message, "UTF-8"));
        }
    }
}
