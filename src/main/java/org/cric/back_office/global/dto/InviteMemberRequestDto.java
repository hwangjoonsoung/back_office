package org.cric.back_office.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InviteMemberRequestDto {

    private String receiverEmail;
    private Long siloId;
    private Long inviterUserId;
}
