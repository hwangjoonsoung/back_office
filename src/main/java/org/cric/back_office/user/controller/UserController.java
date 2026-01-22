package org.cric.back_office.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/")
    public String index() {
        return "/index.html";
    }

    @GetMapping("/user/new")
    public String newUser() {
        return "/new_user.html";
    }
}
