package org.cric.back_office.work.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.cric.back_office.work.entity.Project;

@Setter
@Getter
public class ProjectResponseDto {

    private String name;

    public ProjectResponseDto(String name) {
        this.name = name;
    }

    public ProjectResponseDto(Project project) {
        this.name = project.getName();
    }

}
