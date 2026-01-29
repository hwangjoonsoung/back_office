package org.cric.back_office.global.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cric.back_office.user.entity.User;
import org.cric.back_office.work.entity.Silo;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "silo_invite_history")
public class SiloInviteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "silo_id", nullable = false)
    private Silo silo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mail_history_id")
    private MailHistory mailHistoryId;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "invite_code", length = 10)
    private String inviteCode;

    @Column(name = "date_of_expired", nullable = false)
    private LocalDateTime dateOfExpired;

}
