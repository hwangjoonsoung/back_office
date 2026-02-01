package org.cric.back_office.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Spring Security Authentication의 Principal로 사용될 사용자 정보 객체
 * JWT 토큰에서 추출한 사용자 정보를 담고 있음
 */
@Getter
@AllArgsConstructor
public class UserPrincipal {

    private final Long userId; // User PK
    private final String email; // 사용자 이메일
    private final String name; // 사용자 이름
    private final String role; // 사용자 권한

}
