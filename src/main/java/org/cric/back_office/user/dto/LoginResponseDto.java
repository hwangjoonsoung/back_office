package org.cric.back_office.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cric.back_office.user.enums.UserRole;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private UserRole userRoll;

    public LoginResponseDto(String accessToken, String refreshToken, UserRole userRoll) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = "Bearer";
        this.userRoll = userRoll;
    }
}
