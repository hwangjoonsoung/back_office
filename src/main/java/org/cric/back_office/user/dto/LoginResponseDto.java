package org.cric.back_office.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private String accessToken;
    private String tokenType = "Bearer";
}
