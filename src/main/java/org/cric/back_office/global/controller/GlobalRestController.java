package org.cric.back_office.global.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.cric.back_office.global.dto.ApiResponse;
import org.cric.back_office.global.dto.InviteMemberRequestDto;
import org.cric.back_office.global.dto.InviteMemberResponseDto;
import org.cric.back_office.global.dto.SendingMailInfo;
import org.cric.back_office.global.dto.SendingMailResult;
import org.cric.back_office.global.service.GlobalService;
import org.cric.back_office.global.service.MailSendServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.channels.Pipe;

@RestController
@RequiredArgsConstructor
public class GlobalRestController {

    private final GlobalService globalService;
    private final MailSendServiceImpl mailSendService;

    @PostMapping("/api/mail/invite/member")
    public ResponseEntity<ApiResponse<InviteMemberResponseDto>> inviteSiloToMember(
            @RequestBody InviteMemberRequestDto request) {
        InviteMemberResponseDto result = globalService.inviteMemberToSilo(request);
        ApiResponse<InviteMemberResponseDto> response = new ApiResponse<>(
                result.isSuccess() ? "ok" : "fail",
                result.isSuccess() ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value(),
                result);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping("/api/mail/invite/nonmember/{email}")
    public void inviteSiloToNonMember(@RequestBody SendingMailInfo sendingMaiInfo) {

    }

}
