package org.cric.back_office.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRegistDto {

    private String email;
    private String password;
    private String name;
    private String affiliation;
    private String position;
    private String phoneNumber;
    private LocalDate birthday;

}
