package org.cric.back_office.global.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendingMailInfo extends MailInfo {

    private String requestedUserName;
    private String siloName;

}
