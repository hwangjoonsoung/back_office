package org.cric.back_office.work.dto;

import lombok.Getter;
import org.cric.back_office.work.entity.Silo;

import java.time.LocalDateTime;

@Getter
public class SiloResponseDto {

    private Long id;
    private String name;
    private String createBy;
    private String updateBy;
    private LocalDateTime dateOfCreate;
    private LocalDateTime dateOfUpdate;

    public SiloResponseDto(Silo silo) {
        this.id = silo.getId();
        this.name = silo.getName();
        this.createBy = silo.getCreateBy();
        this.updateBy = silo.getUpdateBy();
        this.dateOfCreate = silo.getDateOfCreate();
        this.dateOfUpdate = silo.getDateOfUpdate();
    }

}
