package org.cric.back_office.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cric.back_office.user.enums.ServiceUserRoll;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private ServiceUserRoll userRoll;

    public LoginResponseDto(String accessToken, String refreshToken, ServiceUserRoll userRoll) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = "Bearer";
        this.userRoll = userRoll;
    }
}
