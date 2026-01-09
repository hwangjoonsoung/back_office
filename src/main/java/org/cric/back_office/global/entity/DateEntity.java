package org.cric.back_office.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class DateEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime dateOfCreate;
    @LastModifiedDate
    private LocalDateTime dateOfUpdate;

}
