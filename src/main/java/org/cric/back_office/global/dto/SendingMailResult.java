package org.cric.back_office.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SendingMailResult {
    private String receiver;
    private HttpStatus httpStatus;
}
