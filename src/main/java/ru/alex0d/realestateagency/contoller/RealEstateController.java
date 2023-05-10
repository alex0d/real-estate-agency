package ru.alex0d.realestateagency.contoller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.alex0d.realestateagency.model.RealEstate;
import ru.alex0d.realestateagency.model.User;
import ru.alex0d.realestateagency.service.RealEstateService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/real-estate")
@RequiredArgsConstructor
public class RealEstateController {
    private final RealEstateService realEstateService;

    @GetMapping
    public String realEstatesByQuery(@RequestParam(name = "q", required = false) String query, Model model) {
        addUserAttributesAndReturnUser(model);

        if (query != null && !query.isBlank()) {
            List<RealEstate> realEstates = realEstateService.search(query);
            model.addAttribute("query", query);
            model.addAttribute("realEstates", realEstates);
            return "real-estate/index";
        } else {
            List<RealEstate> realEstates = realEstateService.findAll();
            model.addAttribute("realEstates", realEstates);
            return "real-estate/index";
        }
    }

    @GetMapping("/{id}")
    public String realEstateInfo(@PathVariable("id") Long id, Model model) {
        addUserAttributesAndReturnUser(model);

        RealEstate realEstate = realEstateService.findById(id);
        model.addAttribute("realEstate", realEstate);
        return "real-estate/info";
    }

    @GetMapping("/my")
    public String myRealEstates(Model model) {
        User user = addUserAttributesAndReturnUser(model).orElseThrow();
        List<RealEstate> realEstates = realEstateService.findByOwner(user);
        model.addAttribute("realEstates", realEstates);
        model.addAttribute("newRealEstate", new RealEstate());
        return "real-estate/user-estates";
    }

    @GetMapping("/edit/{id}")
    public String editRealEstate(@PathVariable("id") Long id, Model model) {
        User user = addUserAttributesAndReturnUser(model).orElseThrow();
        RealEstate realEstate = realEstateService.findById(id);
        if (realEstate.getOwner().getId().equals(user.getId())) {
            model.addAttribute("realEstate", realEstate);
            return "real-estate/edit";
        }
        return "redirect:/real-estate";
    }

    @PutMapping("/{id}")
    public String updateRealEstate(@PathVariable("id") Long id, @ModelAttribute("realEstate") RealEstate realEstate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (!principal.equals("anonymousUser")) {
                User user = (User) principal;
                if (realEstateService.findById(id).getOwner().getId().equals(user.getId())
                        || user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
                    realEstate.setId(id);
                    realEstate.setOwner(user);
                    realEstateService.save(realEstate);
                }
            }
        }
        return "redirect:/real-estate/my";
    }

    @PostMapping
    public String create(@ModelAttribute("realEstate") RealEstate realEstate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (!principal.equals("anonymousUser")) {
                User user = (User) principal;
                realEstate.setOwner(user);
                realEstateService.save(realEstate);
                return "redirect:/real-estate/my";
            }
        }
        return "redirect:/real-estate";
    }

    @DeleteMapping("/{id}")
    public @ResponseBody void delete(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (!principal.equals("anonymousUser")) {
                User user = (User) principal;
                if (realEstateService.findById(id).getOwner().getId().equals(user.getId())
                        || user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
                    realEstateService.deleteById(id);
                }
            }
        }
    }

    private Optional<User> addUserAttributesAndReturnUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (!principal.equals("anonymousUser")) {
                User user = (User) principal;
                if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
                    model.addAttribute("isAdmin", true);
                }
                model.addAttribute("user", user);
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }


}
