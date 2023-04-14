package com.ql.appquanly.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ql.appquanly.model.AppUser;
import com.ql.appquanly.model.Employee;
import com.ql.appquanly.model.Role;
import com.ql.appquanly.repository.DepartmentRepo;
import com.ql.appquanly.repository.EmployeeRepo;
import com.ql.appquanly.repository.RoleRepo;
import com.ql.appquanly.repository.UserRepo;
import com.ql.appquanly.service.UserService;

import jakarta.validation.Valid;

@Controller
public class EmployeeController {
    @Autowired
    EmployeeRepo employeeRepo;

    @Autowired
    DepartmentRepo departmentRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    UserService userService;

    @GetMapping("/employee")
    public String showListEmployee(Model model, Principal principal,
            @RequestParam(value = "id", required = false, defaultValue = "999") Long id) {

        if (id != 999) {
            model.addAttribute("listEmployee", departmentRepo.findById(id).get().getListEmployee());
        } else
            model.addAttribute("listEmployee", employeeRepo.findAll());

        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        String role = loginedUser.getAuthorities().toString();
        model.addAttribute("role", role);
        model.addAttribute("loginedUser", loginedUser);
        model.addAttribute("employee", new Employee());
        model.addAttribute("listDepartment", departmentRepo.findAll());
        return "employee.html";
    }

    @PostMapping("/addEmployee")
    public String addEmployee(@ModelAttribute @Valid Employee employee, Errors errors,
            @RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {

        try {

            if (null != errors && errors.getErrorCount() > 0) {
                return "falied";
            } else {
                String passEncrypt = (new BCryptPasswordEncoder()).encode(password);
                AppUser user = new AppUser();
                user.setPassword(passEncrypt);
                user.setUsername(username);
                userRepo.save(user);
                employeeRepo.save(employee);
                employee.setAppUser(user);
                user.setEmployee(employee);
                userRepo.save(user);
                employeeRepo.save(employee);

                // user.setListRole(roles);
                user.setEnabled(true);

                Role role = roleRepo.findById((long) 2).get();
                role.getListAppUser().add(user);
                roleRepo.save(role);
                return "redirect:/employee";
            }
        } catch (Exception e) {
            return "failed";
        }
    }

    @PostMapping("/deleteEmployee/{id}")
    // @GetMapping("/deleteEmployee")
    public String deleteEmployee(
            @PathVariable("id") Long id_employee) {

        employeeRepo.deleteById(id_employee);
        return "redirect:/employee";
    }

    @PostMapping("/editEmployee")
    public String editEmployee(Model model, Principal principal,
            @RequestParam(value = "id", required = false) Long id) {
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        String role = loginedUser.getAuthorities().toString();
        model.addAttribute("role", role);
        model.addAttribute("loginedUser", loginedUser);
        model.addAttribute("employee", employeeRepo.findById(id).get());
        model.addAttribute("phongBanList", departmentRepo.findAll());
        // model.addAttribute("employee", new Employee());

        return "editEmployee";
    }

    @PostMapping("/editEmployee/{id}")
    public String edit(Model model,
            @PathVariable("id") Long id, @ModelAttribute(value = "employee") Employee employee,
            @ModelAttribute(value = "password") String password) {

        AppUser user = employeeRepo.findById(id).get().getAppUser();

        employee.setAppUser(user);
        employee.setId(id);

        employeeRepo.save(employee);
        return "redirect:/";
    }

}
