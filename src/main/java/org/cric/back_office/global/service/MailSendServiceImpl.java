package org.cric.back_office.global.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.cric.back_office.global.dto.SendingMailInfo;
import org.cric.back_office.global.dto.SendingMailResult;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailSendServiceImpl implements MailSendService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    public SendingMailResult sendMailtoSingleReceiver(SendingMailInfo sendingMailInfo) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(sendingMailInfo.getReceiver());
            helper.setSubject(generateSubjectUsingMailCode(sendingMailInfo));

            String htmlContent = generateTextUsingMailCode(sendingMailInfo);
            helper.setText(htmlContent, true); // true = HTML 형식

            javaMailSender.send(mimeMessage);
            httpStatus = HttpStatus.OK;
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return new SendingMailResult(sendingMailInfo.getReceiver(), httpStatus);
    }

    private String generateSubjectUsingMailCode(SendingMailInfo sendingMailInfo) {
        String subject = "";

        if ("inviteSilo".equals(sendingMailInfo.getMailCode())) {
            subject = sendingMailInfo.getRequestedUserName() + "님으로부터 " + sendingMailInfo.getSiloName()
                    + " 초대가 도착했습니다.";
        }

        return subject;
    }

    private String generateTextUsingMailCode(SendingMailInfo sendingMailInfo) {
        Context context = new Context();

        // 공통 변수 설정
        context.setVariable("siloName", sendingMailInfo.getSiloName());
        context.setVariable("inviterName", sendingMailInfo.getRequestedUserName());
        context.setVariable("inviteCode", "testcode");
        context.setVariable("inviteUrl", "naver.com");

        // mailCode에 따라 해당하는 템플릿 렌더링
        if ("inviteSilo".equals(sendingMailInfo.getMailCode())) {
            return templateEngine.process("mailform/invite_silo", context);
        }

        return "";
    }
}
