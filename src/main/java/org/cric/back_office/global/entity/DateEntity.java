package org.cric.back_office.global.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public class DateEntity {

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        dateOfCreate = now;
        dateOfUpdate = now;
    }

    @PreUpdate
    public void preUpdate() {
        LocalDateTime now = LocalDateTime.now();
        dateOfUpdate = now;
    }
    @CreatedDate
    @Column(updatable = false,name = "data_of_create")
    private LocalDateTime dateOfCreate;
    @LastModifiedDate
    @Column(name = "date_of_update")
    private LocalDateTime dateOfUpdate;

}
