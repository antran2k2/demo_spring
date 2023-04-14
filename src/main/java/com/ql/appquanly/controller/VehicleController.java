package com.ql.appquanly.controller;

import java.security.Principal;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ql.appquanly.model.Employee;
import com.ql.appquanly.model.Vehicle;
import com.ql.appquanly.repository.EmployeeRepo;
import com.ql.appquanly.repository.UserRepo;
import com.ql.appquanly.repository.VehicleRepo;

import jakarta.validation.Valid;
import lombok.val;

@Controller
public class VehicleController {

    @Autowired
    VehicleRepo vehicleRepo;

    @Autowired
    EmployeeRepo employeeRepo;

    @Autowired
    UserRepo userRepo;

    @GetMapping("/vehicle")
    public String getList(Model model, Principal principal) {

        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String role = loginedUser.getAuthorities().toString();
        if (userRepo.findByUsername(loginedUser.getUsername()).getEmployee() != null) {
            Employee e = userRepo.findByUsername(loginedUser.getUsername()).getEmployee();
            model.addAttribute("idEmployee", e.getId());
            model.addAttribute("cccd", e.getCccd());
            model.addAttribute("listVehicle", e.getListVehicle());
        } else {
            model.addAttribute("listVehicle", vehicleRepo.findAll());
        }
        model.addAttribute("loginedUser", loginedUser);
        model.addAttribute("role", role);
        model.addAttribute("vehicle", new Vehicle());
        // model.addAttribute("phongBanList", phongBanService.findAll());
        return "vehicle";
    }

    @GetMapping("/addVehicle")
    public String addPhongBan(@ModelAttribute(value = "vehicle") @Valid Vehicle vehicle, Errors errors,
            @RequestParam(value = "cccd", required = true) String cccd) {

        // try {

        if ((null != errors && errors.getErrorCount() > 0) || employeeRepo.findByCccd(cccd) == null) {
            return "failed";
        } else {
            vehicle.setEmployee(employeeRepo.findByCccd(cccd));
            vehicleRepo.save(vehicle);
            return "redirect:/vehicle";
        }
        // } catch (Exception e) {
        // return "failed";
        // }
    }

    @PostMapping("/deleteVehicle/{id}")
    // @GetMapping("/deleteEmployee")
    public String deleteEmployee(
            @PathVariable("id") Long id) {

        vehicleRepo.deleteById(id);
        return "redirect:/vehicle";
    }

    @GetMapping("/editVehicle")
    public String editEmployee(Model model, Principal principal,
            @RequestParam(value = "id", required = false) Long id) {
        User loginedUser = (User) ((Authentication) principal).getPrincipal();
        String role = loginedUser.getAuthorities().toString();
        model.addAttribute("role", role);
        model.addAttribute("loginedUser", loginedUser);
        model.addAttribute("vehicle", vehicleRepo.findById(id).get());

        // model.addAttribute("employee", vehicleRepo.findById(id).get().getEmployee());
        // model.addAttribute("employee", new Employee());

        return "editVehicle";
    }

    @PostMapping("/editVehicle/{id}")
    public String edit(Model model,
            @PathVariable("id") Long id, @ModelAttribute(value = "vehicle") Vehicle vehicle,
            @RequestParam(value = "cccd", required = true) String cccd) {

        vehicle.setEmployee(employeeRepo.findByCccd(cccd));
        vehicle.setId(id);
        vehicleRepo.save(vehicle);
        return "redirect:/vehicle";
    }
}
