
CREATE TABLE `task_file`
(
    `id`      Bigint NOT NULL,
    `task_id` Bigint NOT NULL,
    `file_id` Bigint NOT NULL
);

CREATE TABLE `task`
(
    `id`             Bigint NOT NULL,
    `silo_id`        Bigint NOT NULL,
    `project_id`     Bigint NOT NULL,
    `title`          varchar(500) NULL,
    `content`        text NULL,
    `status`         char(1) NULL,
    `is_deleted`     boolean NULL,
    `date_of_create` datetime NULL,
    `date_of_update` datetime NULL,
    `create_by`      varchar(20) NULL,
    `update_by`      varchar(20) NULL
);

CREATE TABLE `user_project_roll`
(
    `id`             Bigint NOT NULL,
    `user_id`        Bigint NOT NULL,
    `project_id`     Bigint NOT NULL,
    `role`           varchar(20) NULL,
    `date_of_create` datetime NULL,
    `date_of_update` datetime NULL,
    `create_by`      varchar(20) NULL,
    `update_by`      varchar(20) NULL
);

CREATE TABLE `user`
(
    `id`             Bigint NOT NULL,
    `email`          varchar(100) NULL,
    `password`       varchar(100) NULL,
    `name`           varchar(100) NULL,
    `affiliation`    varchar(100) NULL,
    `position`       varchar(100) NULL,
    `phone_number`   varchar(20) NULL,
    `birth_day`      date NULL,
    `user_status`    varchar(10) NULL,
    `user_roll`      varchar(10) NULL,
    `date_of_create` datetime NULL,
    `date_of_update` datetime NULL,
    `create_by`      varchar(20) NULL,
    `update_by`      varchar(20) NULL
);

CREATE TABLE `silo`
(
    `id`             Bigint NOT NULL,
    `name`           varchar(100) NULL,
    `enable`         boolean NULL,
    `date_of_create` datetime NULL,
    `date_of_update` datetime NULL,
    `create_by`      varchar(20) NULL,
    `update_by`      varchar(20) NULL
);

CREATE TABLE `project`
(
    `id`                 Bigint NOT NULL,
    `silo_id`            Bigint NOT NULL,
    `name`               varchar(500) NULL,
    `is_publish`         boolean NULL,
    `require_approval`   boolean NULL,
    `task_view_auth`     char(1) NULL,
    `task_write_auth`    char(1) NULL,
    `task_update_auth`   char(1) NULL,
    `comment_write_auth` char(1) NULL,
    `comment_view_auth`  char(1) NULL,
    `date_of_create`     datetime NULL,
    `date_of_update`     datetime NULL,
    `create_by`          varchar(20) NULL,
    `update_by`          varchar(20) NULL,
    `is_deleted`         boolean NULL
);

CREATE TABLE `refresh_token`
(
    `id`            Bigint NOT NULL,
    `user_id`        Bigint NOT NULL,
    `token`          varchar(500) NULL,
    `expiry_date`    datetime NULL,
    `date_of_create` datetime NULL,
    `date_of_update` datetime NULL
);

CREATE TABLE `mail_history`
(
    `id`           Bigint NOT NULL,
    `user_id`      Bigint NOT NULL,
    `sender`       varchar(100) NULL,
    `receiver`     varchar(100) NULL,
    `mail_type`    varchar(20) NULL,
    `date_of_send` datetime NULL,
    `is_success`   boolean NULL
);

CREATE TABLE `file`
(
    `id`                 Bigint NOT NULL,
    `original_file_name` varchar(100) NULL,
    `stored_file_name`   varchar(100) NULL,
    `size`               int(20)	NULL,
    `upload_path`        varchar(50) NULL,
    `extension`          char(5) NULL,
    `is_deleted`             boolean NULL,
    `date_of_create`     datetime NULL,
    `date_of_update`     datetime NULL,
    `create_by`          varchar(20) NULL,
    `update_by`          varchar(20) NULL
);

CREATE TABLE `user_silo`
(
    `id`      bigint NOT NULL,
    `silo_id` Bigint NOT NULL,
    `user_id` Bigint NOT NULL,
    `invitable` boolean NOT NULL
);

-- 1. task 테이블 연관관계 (silo, project 참조)
ALTER TABLE task
    ADD CONSTRAINT FK_task_silo FOREIGN KEY (silo_id) REFERENCES silo (id),
    ADD CONSTRAINT FK_task_project FOREIGN KEY (project_id) REFERENCES project(id);

-- 2. user_project_roll 테이블 연관관계 (silo, user, project 참조)
ALTER TABLE user_project_roll
    ADD CONSTRAINT FK_upr_silo FOREIGN KEY (silo_id) REFERENCES silo (id),
    ADD CONSTRAINT FK_upr_user FOREIGN KEY (user_id) REFERENCES user(id),
    ADD CONSTRAINT FK_upr_project FOREIGN KEY (project_id) REFERENCES project(id);

-- 3. project 테이블 연관관계 (silo 참조)
ALTER TABLE project
    ADD CONSTRAINT FK_project_silo FOREIGN KEY (silo_id) REFERENCES silo (id);

-- 4. refresh_token 테이블 연관관계 (user 참조)
ALTER TABLE refresh_token
    ADD CONSTRAINT FK_rt_user FOREIGN KEY (user_id) REFERENCES user (id);

-- 5. mail_history 테이블 연관관계 (user 참조)
ALTER TABLE mail_history
    ADD CONSTRAINT FK_mail_user FOREIGN KEY (user_id) REFERENCES user (id);

-- 6. task_file 테이블 연관관계 (task, file 참조)
ALTER TABLE task_file
    ADD CONSTRAINT FK_tf_task FOREIGN KEY (task_id) REFERENCES task (id),
    ADD CONSTRAINT FK_tf_file FOREIGN KEY (file_id) REFERENCES file(id);