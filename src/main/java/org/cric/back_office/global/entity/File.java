package org.cric.back_office.global.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends EditorEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "original_file_name")
    private String originalFileName;

    @Column(nullable = false, name = "stored_file_name", length = 100)
    private String storedFileName;

    @Column(nullable = false, length = 20)
    private Integer size;

    @Column(nullable = false, length = 50)
    private String uploadPath;

    @Column(nullable = false, name = "parent_type", length = 20)
    private String parentType;

    @Column(length = 100, nullable = false,name = "parent_id")
    private Long parentId;
}
