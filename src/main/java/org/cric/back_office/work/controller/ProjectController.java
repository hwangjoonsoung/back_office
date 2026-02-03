package org.cric.back_office.work.controller;

import lombok.RequiredArgsConstructor;
import org.cric.back_office.global.dto.UserPrincipal;
import org.cric.back_office.work.dto.ProjectResponseDto;
import org.cric.back_office.work.service.ProjectService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/project/{projectId}")
    public String projectList(@PathVariable("projectId") Long projectId, Model model) {
        /**
         * 여기서는 task를 가져와야 함
         **/
        List<ProjectResponseDto> projectDtoList = projectService.getProjectList(projectId);
        model.addAttribute("projectList", projectDtoList);
        return "/project/index.html";
    }
}
