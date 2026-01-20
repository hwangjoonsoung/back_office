package org.cric.back_office.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.cric.back_office.global.entity.EditorEntity;
import org.cric.back_office.user.dto.UserEditDto;
import org.cric.back_office.user.dto.UserRegistDto;
import org.cric.back_office.user.enums.UserRole;
import org.cric.back_office.user.enums.UserStatus;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends EditorEntity {

    public static User createUser(UserRegistDto userRegistDto) {
        User user = new User();
        return user.settingRegistUser(userRegistDto);
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 100, name = "email")
    private String email;
    @Column(nullable = false, length = 100, name = "password")
    private String password;
    @Column(nullable = false, length = 100, name = "name")
    private String name;
    @Column(length = 100, name = "affiliation")
    private String affiliation;
    @Column(length = 100, name = "position")
    private String position;
    @Column(nullable = false, length = 20, unique = true, name = "phone_name")
    private String phoneNumber;
    @Column(nullable = false, name = "birth_day")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "user_status", length = 10)
    private UserStatus userStatus =UserStatus.APPROVED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "user_roll", length = 10)
    private UserRole userRole = UserRole.GENERAL;

    public User settingRegistUser(UserRegistDto userRegistDto) {
        this.email = userRegistDto.getEmail();
        this.password = userRegistDto.getPassword();
        this.name = userRegistDto.getName();
        this.affiliation = userRegistDto.getAffiliation();
        this.position = userRegistDto.getPosition();
        this.phoneNumber = userRegistDto.getPhoneNumber();
        this.birthday = userRegistDto.getBirthday();

        setCreateBy(userRegistDto.getName());
        setUpdateBy(userRegistDto.getName());
        return this;
    }

    public User settingEditUser(User user, UserEditDto userEditDto) {
        user.name = userEditDto.getName();
        user.affiliation = userEditDto.getAffiliation();
        user.position = userEditDto.getPosition();
        user.phoneNumber = userEditDto.getPhoneNumber();
        user.birthday = userEditDto.getBirthday();

        return user;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
}
