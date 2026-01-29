package org.cric.back_office.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InviteMemberResponseDto {

    private boolean success;
    private String message;
    private String errorCode;

    /**
     * 성공 응답 생성
     */
    public static InviteMemberResponseDto success(String message) {
        return new InviteMemberResponseDto(true, message, null);
    }

    /**
     * 실패 응답 생성
     */
    public static InviteMemberResponseDto failure(String message, String errorCode) {
        return new InviteMemberResponseDto(false, message, errorCode);
    }
}
