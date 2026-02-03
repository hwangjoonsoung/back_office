package org.cric.back_office.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @GetMapping("/")
    public String index() {
        return "/index.html";
    }

    @GetMapping("/user/new")
    public String newUser() {
        return "/user/new_user.html";
    }

    @GetMapping("/user/login")
    public String longinUser() {
        return "/user/user_login.html";
    }

    @GetMapping("/global/error.html")
    public String errorPage(@RequestParam(required = false, defaultValue = "unknown") String error,
            @RequestParam(required = false, defaultValue = "오류가 발생했습니다") String message,
            Model model) {
        model.addAttribute("errorCode", error);
        model.addAttribute("errorMessage", message);
        return "/global/error.html";
    }
}
