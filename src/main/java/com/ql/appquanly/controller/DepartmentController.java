package com.ql.appquanly.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ql.appquanly.model.Department;
import com.ql.appquanly.repository.DepartmentRepo;
import com.ql.appquanly.repository.UserRepo;

import jakarta.validation.Valid;

@Controller
public class DepartmentController {

    @Autowired
    DepartmentRepo departmentRepo;
    @Autowired
    UserRepo userRepo;

    @GetMapping("/department")
    public String homeDepartment(Model model, Principal principal,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Long limit) {

        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String role = loginedUser.getAuthorities().toString();
        if (userRepo.findByUsername(loginedUser.getUsername()).getEmployee() != null) {

            model.addAttribute("idEmployee", userRepo.findByUsername(loginedUser.getUsername()).getEmployee().getId());
        }
        model.addAttribute("loginedUser", loginedUser);
        model.addAttribute("role", role);
        model.addAttribute("listDepartment", departmentRepo.findAll());
        model.addAttribute("department", new Department());
        return "department";
    }

    @PostMapping("/addDepartment")
    public String addDepartment(@ModelAttribute @Valid Department department, Errors errors) {

        if (null != errors && errors.getErrorCount() > 0) {
            return "failed";
        } else {
            departmentRepo.save(department);
            return "redirect:/department";
        }

    }

    @PostMapping("/deleteDepartment/{id}")
    public String deleteDepartment(
            @PathVariable("id") Long id) {

        departmentRepo.deleteById(id);
        return "redirect:/department";
    }

    // @GetMapping("/editDepartment")
    // public Department edit(Model model, @RequestParam(value = "id") Long id) {

    // model.addAttribute("department", departmentRepo.findById(id).get());
    // return departmentRepo.findById(id).get();
    // }
    @GetMapping("/editDepartment")
    public String edit(Model model, Principal principal, @RequestParam(value = "id") Long id) {

        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        model.addAttribute("loginedUser", loginedUser);
        model.addAttribute("department", departmentRepo.findById(id).get());
        return "editDepartment";
    }

    @PostMapping("/editDepartment/{id}")
    public String edit(Model model,
            @PathVariable("id") Long id, @Valid @ModelAttribute(value = "department") Department department,
            Errors errors) {

        if (null != errors && errors.getErrorCount() > 0) {
            return "failed";
        } else {

            department.setId(id);
            departmentRepo.save(department);
            return "redirect:/department";
        }

    }

}
