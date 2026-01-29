package org.cric.back_office.work.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cric.back_office.global.entity.EditorEntity;
import org.cric.back_office.user.entity.UserSilo;
import org.cric.back_office.work.dto.SiloCreateDto;
import org.cric.back_office.work.dto.SiloUpdateDto;

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

    @Column(nullable = false, name = "is_deleted")
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "silo", fetch = FetchType.LAZY)
    private List<UserSilo> userSilo;

    // 연관관계: Project
    @OneToMany(mappedBy = "silo", fetch = FetchType.LAZY)
    private List<Project> projects = new ArrayList<>();

    // 연관관계: Task
    @OneToMany(mappedBy = "silo", fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    /**
     * Silo 생성 팩토리 메서드
     */
    public static Silo createSilo(SiloCreateDto dto) {
        Silo silo = new Silo();
        silo.name = dto.getName();
        return silo;
    }

    /**
     * Silo 정보 수정 메서드
     */
    public void updateSilo(SiloUpdateDto dto) {
        if (dto.getName() != null) {
            this.name = dto.getName();
        }
    }

    /**
     * Silo 삭제 (논리 삭제)
     */
    public void delete() {
        this.isDeleted = true;
    }

}
