package org.cric.back_office.work.controller;

import lombok.RequiredArgsConstructor;
import org.cric.back_office.work.dto.ProjectResponseDto;
import org.cric.back_office.work.repository.ProjectJpaRepository;
import org.cric.back_office.work.service.ProjectService;
import org.cric.back_office.work.service.SiloService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SiloController {

    private final SiloService siloService;
    private final ProjectService projectService;

    @GetMapping("/silo/{id}")
    public String gotoSiloMain(@PathVariable(name = "id") Long id, Model model) {
        // silo main으로 이동하는 과정
        /**
         *  필요한것
         *  project, silo 초대 권한
         **/
        List<ProjectResponseDto> projectList = projectService.getProjectList(id);
        model.addAttribute("prjectList", projectList);
        return "/silo/silo_main.html";
    }
}
