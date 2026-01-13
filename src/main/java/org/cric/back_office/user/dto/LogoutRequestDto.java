package org.cric.back_office.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequestDto {

    @NotNull(message = "사용자 ID는 필수입니다")
    private Integer userId;
}
