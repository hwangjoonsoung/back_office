package org.cric.back_office.work.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cric.back_office.global.entity.EditorEntity;
import org.cric.back_office.user.entity.UserProjectRoll;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "project")
public class Project extends EditorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "silo_id", nullable = false)
    private Silo silo;

    @Column(nullable = false, length = 500)
    private String name;

    @Column(name = "is_publish", nullable = false)
    private Boolean isPublish;

    @Column(name = "require_approval", nullable = false)
    private Boolean requireApproval;

    @Column(name = "task_view_auth", nullable = false, length = 1)
    private Character taskViewAuth;

    @Column(name = "task_write_auth", nullable = false, length = 1)
    private Character taskWriteAuth;

    @Column(name = "task_update_auth", nullable = false, length = 1)
    private Character taskUpdateAuth;

    @Column(name = "comment_write_auth", nullable = false, length = 1)
    private Character commentWriteAuth;

    @Column(name = "comment_view_auth", nullable = false, length = 1)
    private Character commentViewAuth;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    // 연관관계: Task
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    // 연관관계: UserProjectRoll
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<UserProjectRoll> userProjectRolls = new ArrayList<>();

    // 연관관계 편의메서드(Project - Silo)
    public void changeSilo(Silo silo) {
        this.silo = silo;
        silo.getProjects().add(this);
    }
}
