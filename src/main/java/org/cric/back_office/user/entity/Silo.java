package org.cric.back_office.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cric.back_office.global.entity.EditorEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "silo")
public class Silo extends EditorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Boolean enable;

    // 연관관계: Project
    @OneToMany(mappedBy = "silo", fetch = FetchType.LAZY)
    private List<Project> projects = new ArrayList<>();

    // 연관관계: Task
    @OneToMany(mappedBy = "silo", fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    // 연관관계: UserProjectRoll
    @OneToMany(mappedBy = "silo", fetch = FetchType.LAZY)
    private List<UserProjectRoll> userProjectRolls = new ArrayList<>();
}
