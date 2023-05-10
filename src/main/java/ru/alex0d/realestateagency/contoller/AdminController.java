package ru.alex0d.realestateagency.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.alex0d.realestateagency.service.RealEstateService;
import ru.alex0d.realestateagency.service.UserService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final RealEstateService realEstateService;

    @GetMapping
    public String options() {
        return "admin/options";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/real-estates")
    public String realEstates(Model model) {
        model.addAttribute("realEstates", realEstateService.findAll());
        return "admin/real-estates";
    }

    @DeleteMapping("/users/{id}")
    public @ResponseBody void deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
    }

    @DeleteMapping("/real-estates/{id}")
    public @ResponseBody void deleteRealEstate(@PathVariable("id") Long id) {
        realEstateService.deleteById(id);
    }
}
