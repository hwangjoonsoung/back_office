package org.cric.back_office.user.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserEditDto {

    @NotBlank(message = "이름은 필수입니다")
    @Size(max = 100, message = "이름은 100자 이하여야 합니다")
    private String name;

    @Size(max = 100, message = "소속은 100자 이하여야 합니다")
    private String affiliation;

    @Size(max = 100, message = "직책은 100자 이하여야 합니다")
    private String position;

    @NotBlank(message = "전화번호는 필수입니다")
    @Size(max = 20, message = "전화번호는 20자 이하여야 합니다")
    private String phoneNumber;

    @NotNull(message = "생년월일은 필수입니다")
    private LocalDate birthday;
}
