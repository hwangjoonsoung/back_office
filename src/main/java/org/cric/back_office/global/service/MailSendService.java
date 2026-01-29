package org.cric.back_office.global.service;

import org.cric.back_office.global.dto.SendingMailInfo;
import org.cric.back_office.global.dto.SendingMailResult;

public interface MailSendService {

    SendingMailResult sendMailtoSingleReceiver(SendingMailInfo sendingMaiInfo);
}
