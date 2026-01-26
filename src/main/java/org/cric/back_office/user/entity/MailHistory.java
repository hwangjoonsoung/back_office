package org.cric.back_office.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mail_history")
public class MailHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String sender;

    @Column(nullable = false, length = 100)
    private String receiver;

    @Column(name = "mail_type", nullable = false, length = 20)
    private String mailType;

    @Column(name = "date_of_send", nullable = false)
    private LocalDateTime dateOfSend;

    @Column(name = "is_success", nullable = false)
    private Boolean isSuccess;

    //연관관계 편의 메서드
    public void changeUser(User user) {
        this.user = user;
        user.getMailHistories().add(this);
    }
}
