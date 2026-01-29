package org.cric.back_office.global.service;

import lombok.RequiredArgsConstructor;
import org.cric.back_office.global.dto.InviteMemberRequestDto;
import org.cric.back_office.global.dto.InviteMemberResponseDto;
import org.cric.back_office.global.dto.SendingMailInfo;
import org.cric.back_office.global.dto.SendingMailResult;
import org.cric.back_office.user.entity.User;
import org.cric.back_office.user.service.UserService;
import org.cric.back_office.work.dto.SiloResponseDto;
import org.cric.back_office.work.service.SiloService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GlobalService {

    private final UserService userService;
    private final SiloService siloService;
    private final MailSendService mailSendService;

    /**
     * 회원에게 Silo 초대 이메일 발송
     * 1단계: 사용자 검증 (존재 여부 및 APPROVED 상태)
     * 2단계: 검증 성공 시 자동으로 이메일 발송
     */
    public InviteMemberResponseDto inviteMemberToSilo(InviteMemberRequestDto request) {
        // 1단계: 사용자 검증
        User receiverUser = userService.validateUserForInvitation(request.getReceiverEmail());

        if (receiverUser == null) {
            // 사용자가 없거나 승인되지 않은 경우
            return InviteMemberResponseDto.failure(
                    "해당 이메일로 가입된 회원을 찾을 수 없거나 승인되지 않은 회원입니다.",
                    "USER_NOT_FOUND_OR_NOT_APPROVED");
        }
        // Silo 존재 여부 확인
        SiloResponseDto silo;
        try {
            silo = siloService.findById(request.getSiloId());
        } catch (IllegalArgumentException e) {
            return InviteMemberResponseDto.failure(
                    "존재하지 않는 Silo입니다.",
                    "SILO_NOT_FOUND");
        }

        // 초대하는 사람 정보 조회
        User inviterUser;
        try {
            inviterUser = userService.getUserEntityById(request.getInviterUserId());
        } catch (Exception e) {
            return InviteMemberResponseDto.failure(
                    "초대하는 사용자 정보를 찾을 수 없습니다.",
                    "INVITER_NOT_FOUND");
        }

        // 2단계: 이메일 발송
        SendingMailInfo mailInfo = new SendingMailInfo();
        mailInfo.setReceiver(receiverUser.getEmail());
        mailInfo.setMailCode("inviteSilo");
        mailInfo.setSiloName(silo.getName());
        mailInfo.setRequestedUserName(inviterUser.getName());

        SendingMailResult mailResult = mailSendService.sendMailtoSingleReceiver(mailInfo);

        if (mailResult.getHttpStatus() == HttpStatus.OK) {
            return InviteMemberResponseDto.success("초대 이메일이 성공적으로 발송되었습니다.");
        } else {
            return InviteMemberResponseDto.failure(
                    "이메일 발송에 실패했습니다.",
                    "EMAIL_SEND_FAILED");
        }
    }
}
