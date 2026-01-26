package org.cric.back_office.work.controller;

import lombok.RequiredArgsConstructor;
import org.cric.back_office.work.service.SiloService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashBoardController {

    private final SiloService siloService;

    @GetMapping({ "/dashboard/" })
    public String index() {
        return "/dashboard/index.html";
    }
}
