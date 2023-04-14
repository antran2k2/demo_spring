package com.ql.appquanly.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.expression.Lists;

import com.ql.appquanly.model.AppUser;
import com.ql.appquanly.repository.UserRepo;

@Controller
public class MainController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/")
    public String index(Principal principal, Model model) {

        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String role = loginedUser.getAuthorities().toString();
        if (userRepo.findByUsername(loginedUser.getUsername()).getEmployee() != null) {

            model.addAttribute("idEmployee", userRepo.findByUsername(loginedUser.getUsername()).getEmployee().getId());
        }
        model.addAttribute("loginedUser", loginedUser);
        model.addAttribute("role", role);
        return "index";
    }

    @GetMapping("/admin")
    public String adminPage() {

        return "adminPage";
    }

    @GetMapping("/hello")
    public String hello(Principal principal, Model model) {

        String username = principal.getName();

        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        model.addAttribute("username", loginedUser);
        return "hello";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {

        return "loginPage";
    }

    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        return "logoutSuccessfulPage";
    }

    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {

        // Sau khi user login thanh cong se co principal
        String userName = principal.getName();

        System.out.println("User Name: " + userName);

        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        // String userInfo = WebUtils.toString(loginedUser);
        // model.addAttribute("userInfo", userInfo);

        return "userInfoPage";
    }
}
