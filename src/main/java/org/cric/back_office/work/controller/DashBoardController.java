package org.cric.back_office.work.controller;

import lombok.RequiredArgsConstructor;
import org.cric.back_office.global.dto.UserPrincipal;
import org.cric.back_office.work.service.SiloService;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashBoardController {

    private final SiloService siloService;

    @GetMapping({ "/dashboard/" })
    public String index(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        // 사용자 ID로 Silo 리스트 조회
        var siloList = siloService.findByUserId(userPrincipal.getUserId());
        model.addAttribute("siloList", siloList);
        
        return "/dashboard/index.html";
    }
}
