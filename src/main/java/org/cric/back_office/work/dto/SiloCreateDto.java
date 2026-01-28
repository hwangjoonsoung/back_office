package org.cric.back_office.work.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiloCreateDto {

    @NotBlank(message = "Silo 이름은 필수입니다")
    @Size(max = 100, message = "Silo 이름은 100자 이하여야 합니다")
    private String name;

}
