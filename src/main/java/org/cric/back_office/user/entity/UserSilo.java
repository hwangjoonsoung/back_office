package org.cric.back_office.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cric.back_office.work.entity.Silo;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_silo")
public class UserSilo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연관관계: User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    // 연관관계: Silo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "silo_id",nullable = false)
    private Silo silo;

    @Column(name = "invitable")
    private boolean invitable = false;

    // 연관관계 편의메서드
    public void changeUser(User user) {
        this.user = user;
        user.getUserSilos().add(this);
    }

    public void changeSilo(Silo silo) {
        this.silo = silo;
        silo.getUserSilo().add(this);
    }

}
