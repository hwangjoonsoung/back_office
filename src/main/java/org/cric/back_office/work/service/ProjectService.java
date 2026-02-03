package org.cric.back_office.work.service;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.cric.back_office.global.dto.UserPrincipal;
import org.cric.back_office.work.dto.ProjectResponseDto;
import org.cric.back_office.work.entity.Project;
import org.cric.back_office.work.repository.ProjectJpaRepository;
import org.cric.back_office.work.repository.ProjectRepository;
import org.cric.back_office.work.repository.SiloJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectJpaRepository projectJpaRepository;
    private final SiloJpaRepository siloJpaRepository;

    public List<ProjectResponseDto> getProjectList(Long siloId) {
        return projectJpaRepository.findProjectBySiloIdAndIsDeleted(siloId, false)
                .stream()
                .map(ProjectResponseDto::new)
                .collect(Collectors.toList());
    }
}
