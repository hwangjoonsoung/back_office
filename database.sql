
CREATE TABLE task
(
    id             Bigint       NOT NULL primary key auto_increment,
    silo_id        Bigint       not null,
    project_id     Bigint       not null,
    title          varchar(500) not NULL,
    content        text         not NULL,
    status         char(1)      not NULL,
    is_deleted     boolean      not NULL default false,
    date_of_create datetime     not NULL,
    date_of_update datetime     not NULL,
    create_by      varchar(20)  not NULL,
    update_by      varchar(20)  not NULL
);

CREATE TABLE user_project_roll
(
    id             Bigint      NOT NULL primary key,
    silo_id        Bigint      NOT NULL,
    user_id        Bigint      NOT NULL,
    project_id     Bigint      NOT NULL,
    role           varchar(20) not NULL,
    date_of_create datetime    not NULL,
    date_of_update datetime    not NULL,
    create_by      varchar(20) not NULL,
    update_by      varchar(20) not NULL
);

CREATE TABLE user
(
    id             Bigint       NOT NULL primary key auto_increment,
    email          varchar(100) not NULL,
    password       varchar(100) not NULL,
    name           varchar(100) not NULL,
    affiliation    varchar(100) NULL,
    position       varchar(100) NULL,
    phone_number   varchar(20)  NULL,
    birth_day      date         NULL,
    user_status    varchar(10)  not NULL,
    user_roll      varchar(10)  not NULL,
    date_of_create datetime     not NULL,
    date_of_update datetime     not NULL,
    create_by      varchar(20)  not NULL,
    update_by      varchar(20)  not NULL
);

CREATE TABLE silo
(
    id             Bigint       NOT NULL primary key auto_increment,
    name           varchar(100) not NULL,
    enable         boolean      not NULL,
    date_of_create datetime     not NULL,
    date_of_update datetime     not NULL,
    create_by      varchar(20)  not NULL,
    update_by      varchar(20)  not NULL
);

CREATE TABLE project
(
    id                 Bigint       NOT NULL primary key auto_increment,
    silo_id            Bigint       NOT NULL,
    name               varchar(500) not NULL,
    is_publish         boolean      not NULL,
    require_approval   boolean      not NULL,
    task_view_auth     char(1)      not NULL,
    task_write_auth    char(1)      not NULL,
    task_update_auth   char(1)      not NULL,
    comment_write_auth char(1)      not NULL,
    comment_view_auth  char(1)      not NULL,
    date_of_create     datetime     not NULL,
    date_of_update     datetime     not NULL,
    create_by          varchar(20)  not NULL,
    update_by          varchar(20)  not NULL,
    is_deleted         boolean      not NULL default false
);

CREATE TABLE refresh_token
(
    id             Bigint       NOT NULL primary key auto_increment,
    user_id        Bigint       NOT NULL,
    token          varchar(500) not NULL,
    expiry_date    datetime     not NULL,
    date_of_create datetime     not NULL,
    date_of_update datetime     not NULL
);

CREATE TABLE mail_history
(
    id           Bigint       NOT NULL primary key auto_increment,
    user_id      Bigint       NOT NULL,
    sender       varchar(100) not NULL,
    receiver     varchar(100) not NULL,
    mail_type    varchar(20)  not NULL,
    date_of_send datetime     not NULL,
    is_success   boolean      not NULL
);

CREATE TABLE file
(
    id                 Bigint                NOT NULL primary key auto_increment,
    original_file_name varchar(100)          not NULL,
    stored_file_name   varchar(100)          not NULL,
    size               int(20)               not NULL,
    upload_path        varchar(50)           not NULL,
    extension          char(5)               not NULL,
    is_deleted         boolean default false not NULL,
    date_of_create     datetime              not NULL,
    date_of_update     datetime              not NULL,
    create_by          varchar(20)           not NULL,
    update_by          varchar(20)           not NULL
);

CREATE TABLE task_file
(
    id      Bigint NOT NULL primary key auto_increment,
    task_id Bigint NOT NULL,
    file_id Bigint NOT NULL
);

-- 1. task 테이블 연관관계 (silo, project 참조)
ALTER TABLE task
    ADD CONSTRAINT FK_task_silo FOREIGN KEY (silo_id) REFERENCES silo(id),
    ADD CONSTRAINT FK_task_project FOREIGN KEY (project_id) REFERENCES project(id);

-- 2. user_project_roll 테이블 연관관계 (silo, user, project 참조)
ALTER TABLE user_project_roll
    ADD CONSTRAINT FK_upr_silo FOREIGN KEY (silo_id) REFERENCES silo(id),
    ADD CONSTRAINT FK_upr_user FOREIGN KEY (user_id) REFERENCES user(id),
    ADD CONSTRAINT FK_upr_project FOREIGN KEY (project_id) REFERENCES project(id);

-- 3. project 테이블 연관관계 (silo 참조)
ALTER TABLE project
    ADD CONSTRAINT FK_project_silo FOREIGN KEY (silo_id) REFERENCES silo(id);

-- 4. refresh_token 테이블 연관관계 (user 참조)
ALTER TABLE refresh_token
    ADD CONSTRAINT FK_rt_user FOREIGN KEY (user_id) REFERENCES user(id);

-- 5. mail_history 테이블 연관관계 (user 참조)
ALTER TABLE mail_history
    ADD CONSTRAINT FK_mail_user FOREIGN KEY (user_id) REFERENCES user(id);

-- 6. task_file 테이블 연관관계 (task, file 참조)
ALTER TABLE task_file
    ADD CONSTRAINT FK_tf_task FOREIGN KEY (task_id) REFERENCES task(id),
    ADD CONSTRAINT FK_tf_file FOREIGN KEY (file_id) REFERENCES file(id);