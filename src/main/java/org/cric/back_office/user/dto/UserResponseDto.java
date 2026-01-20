package org.cric.back_office.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cric.back_office.user.entity.User;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private String name;
    private String affiliation;
    private String position;
    private String phoneNumber;
    private LocalDate birthday;

    public UserResponseDto settingUserDate(User user) {
        this.affiliation = user.getAffiliation();
        this.name = user.getName();
        this.position = user.getPosition();
        this.phoneNumber = user.getPhoneNumber();
        this.birthday = user.getBirthday();
        return this;
    }

    public UserResponseDto(User user) {
        this.name = user.getName();
        this.affiliation = user.getAffiliation();
        this.position = user.getPosition();
        this.phoneNumber = user.getPhoneNumber();
        this.birthday = user.getBirthday();
    }
}
