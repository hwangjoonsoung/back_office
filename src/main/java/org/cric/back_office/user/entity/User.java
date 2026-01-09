package org.cric.back_office.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.cric.back_office.global.entity.EditorEntity;
import org.cric.back_office.user.enums.UserStatus;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends EditorEntity {

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
    @Column(nullable = false, length = 100, name = "affiliation")
    private String affiliation;
    @Column(nullable = false, length = 100, name = "position")
    private String position;
    @Column(nullable = false, length = 20, unique = true, name = "phone_name")
    private String phoneNumber;

    @Column(nullable = false, name = "birth_day")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "user_status", length = 10)
    private UserStatus userStatus =UserStatus.PENDING;
}
