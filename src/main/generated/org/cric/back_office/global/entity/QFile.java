package org.cric.back_office.global.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFile is a Querydsl query type for File
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFile extends EntityPathBase<File> {

    private static final long serialVersionUID = -536705839L;

    public static final QFile file = new QFile("file");

    public final QEditorEntity _super = new QEditorEntity(this);

    //inherited
    public final StringPath createBy = _super.createBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dateOfCreate = _super.dateOfCreate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dateOfUpdate = _super.dateOfUpdate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath originalFileName = createString("originalFileName");

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public final StringPath parentType = createString("parentType");

    public final NumberPath<Integer> size = createNumber("size", Integer.class);

    public final StringPath sotredFileName = createString("sotredFileName");

    //inherited
    public final StringPath updateBy = _super.updateBy;

    public final StringPath uploadPath = createString("uploadPath");

    public QFile(String variable) {
        super(File.class, forVariable(variable));
    }

    public QFile(Path<? extends File> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFile(PathMetadata metadata) {
        super(File.class, metadata);
    }

}

