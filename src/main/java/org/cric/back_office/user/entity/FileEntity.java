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
@Table(name = "file")
public class FileEntity extends EditorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_file_name", nullable = false, length = 100)
    private String originalFileName;

    @Column(name = "stored_file_name", nullable = false, length = 100)
    private String storedFileName;

    @Column(nullable = false)
    private Integer size;

    @Column(name = "upload_path", nullable = false, length = 50)
    private String uploadPath;

    @Column(nullable = false, length = 5)
    private String extension;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    // 연관관계: TaskFile
    @OneToMany(mappedBy = "file", fetch = FetchType.LAZY)
    private List<TaskFile> taskFiles = new ArrayList<>();
}
