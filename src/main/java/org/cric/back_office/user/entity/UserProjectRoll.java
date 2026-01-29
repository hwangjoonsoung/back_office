package org.cric.back_office.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cric.back_office.global.entity.EditorEntity;
import org.cric.back_office.user.enums.ProjectRole;
import org.cric.back_office.work.entity.Project;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_project_roll")
public class UserProjectRoll extends EditorEntity {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProjectRole role;

    //연관관계 편의 메서드(userProjectRoll - Project)
    public void changeProject(Project project) {
        this.project = project;
        project.getUserProjectRolls().add(this);
    }

    //연관관계 편의 메서드(userProjectRoll - User)
    public void changeUser(User user) {
        this.user = user;
        user.getUserProjectRolls().add(this);
    }

}
