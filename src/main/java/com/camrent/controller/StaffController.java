package com.camrent.controller;

import com.camrent.entity.Customer;
import com.camrent.entity.EquipmentType;
import com.camrent.entity.User;
import com.camrent.service.CustomerService;
import com.camrent.service.EquipmentService;
import com.camrent.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.math.BigDecimal;

@Controller
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffController {

    private final EquipmentService equipmentService;
    private final CustomerService customerService;
    private final RentalService rentalService;

    @GetMapping("/dashboard")
    public String staffDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        model.addAttribute("staffName", user.getName());
        model.addAttribute("cameras", equipmentService.getAvailableEquipmentByType(EquipmentType.CAMERA_BODY));
        model.addAttribute("lenses", equipmentService.getAvailableEquipmentByType(EquipmentType.CAMERA_LENS));
        model.addAttribute("accessories", equipmentService.getAvailableEquipmentByType(EquipmentType.ACCESSORY));
        
        return "staff-dashboard"; // Thymeleaf view in next step
    }

    @PostMapping("/customer/register")
    public String registerCustomer(@ModelAttribute Customer customer) {
        customerService.registerCustomer(customer);
        return "redirect:/staff/dashboard";
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam String customerId, 
                           @RequestParam List<String> equipmentIds, 
                           @RequestParam int days, 
                           HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        rentalService.checkout(user.getUserId(), customerId, equipmentIds, days);
        return "redirect:/staff/dashboard";
    }

    @PostMapping("/checkin")
    public String checkin(@RequestParam String transactionId, 
                          @RequestParam(required = false, defaultValue = "false") boolean isDamaged, 
                          @RequestParam(required = false) String damageDescription, 
                          @RequestParam(required = false) BigDecimal damageCost,
                          @RequestParam String paymentMethod) {
        rentalService.checkIn(transactionId, isDamaged, damageDescription, damageCost, paymentMethod);
        return "redirect:/staff/dashboard";
    }
}
