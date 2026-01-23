package org.cric.back_office.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashBoardController {

    @GetMapping({ "/dashboard/", "/dashboard/index.html" })
    public String index() {
        return "/dashboard/index.html";
    }
}
