package org.cric.back_office.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1889589384L;

    public static final QUser user = new QUser("user");

    public final org.cric.back_office.global.entity.QEditorEntity _super = new org.cric.back_office.global.entity.QEditorEntity(this);

    public final StringPath affiliation = createString("affiliation");

    public final DatePath<java.time.LocalDate> birthday = createDate("birthday", java.time.LocalDate.class);

    //inherited
    public final StringPath createBy = _super.createBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dateOfCreate = _super.dateOfCreate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dateOfUpdate = _super.dateOfUpdate;

    public final StringPath email = createString("email");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath position = createString("position");

    //inherited
    public final StringPath updateBy = _super.updateBy;

    public final EnumPath<org.cric.back_office.user.enums.UserStatus> userStatus = createEnum("userStatus", org.cric.back_office.user.enums.UserStatus.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

