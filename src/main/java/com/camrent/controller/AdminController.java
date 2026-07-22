package com.camrent.controller;

import com.camrent.entity.Equipment;
import com.camrent.entity.EquipmentStatus;
import com.camrent.entity.EquipmentType;
import com.camrent.dto.EquipmentDto;
import com.camrent.service.EquipmentService;
import com.camrent.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final EquipmentService equipmentService;
    private final CustomerService customerService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("equipments", equipmentService.getAllEquipment());
        model.addAttribute("cameras", equipmentService.getEquipmentByType(EquipmentType.CAMERA_BODY));
        model.addAttribute("lenses", equipmentService.getEquipmentByType(EquipmentType.CAMERA_LENS));
        model.addAttribute("accessories", equipmentService.getEquipmentByType(EquipmentType.ACCESSORY));
        model.addAttribute("customers", customerService.getAllCustomers());
        
        return "admin-dashboard"; // Thymeleaf view in next step
    }

    @PostMapping("/equipment/add")
    public String addEquipment(@ModelAttribute EquipmentDto equipmentDto) {
        equipmentService.addEquipment(equipmentDto);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/equipment/remove")
    public String removeEquipment(@RequestParam String equipmentId) {
        equipmentService.removeEquipment(equipmentId);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/equipment/status")
    public String updateStatus(@RequestParam String equipmentId, @RequestParam EquipmentStatus status) {
        equipmentService.updateStatus(equipmentId, status);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/equipment/price")
    public String updatePrice(@RequestParam String equipmentId, @RequestParam BigDecimal price) {
        equipmentService.updateRentalPrice(equipmentId, price);
        return "redirect:/admin/dashboard";
    }
}
