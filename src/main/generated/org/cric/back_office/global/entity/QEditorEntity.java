package org.cric.back_office.global.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEditorEntity is a Querydsl query type for EditorEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QEditorEntity extends EntityPathBase<EditorEntity> {

    private static final long serialVersionUID = 189126149L;

    public static final QEditorEntity editorEntity = new QEditorEntity("editorEntity");

    public final QDateEntity _super = new QDateEntity(this);

    public final StringPath createBy = createString("createBy");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dateOfCreate = _super.dateOfCreate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dateOfUpdate = _super.dateOfUpdate;

    public final StringPath updateBy = createString("updateBy");

    public QEditorEntity(String variable) {
        super(EditorEntity.class, forVariable(variable));
    }

    public QEditorEntity(Path<? extends EditorEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEditorEntity(PathMetadata metadata) {
        super(EditorEntity.class, metadata);
    }

}

