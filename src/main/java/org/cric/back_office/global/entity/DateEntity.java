package org.cric.back_office.global.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class DateEntity {

    @CreatedDate
    @Column(updatable = false,name = "date_of_create")
    private LocalDateTime dateOfCreate;
    @LastModifiedDate
    @Column(name = "date_of_update")
    private LocalDateTime dateOfUpdate;

}
