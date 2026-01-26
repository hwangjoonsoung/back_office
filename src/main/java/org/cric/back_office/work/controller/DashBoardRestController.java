package org.cric.back_office.work.controller;

import lombok.RequiredArgsConstructor;
import org.cric.back_office.work.service.SiloService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DashBoardRestController {

    private final SiloService siloService;

}
