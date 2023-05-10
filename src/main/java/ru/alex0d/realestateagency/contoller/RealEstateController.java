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
        getUserIfAuthenticated().ifPresent(u -> addUserAttributesToModel(u, model));

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
        getUserIfAuthenticated().ifPresent(u -> addUserAttributesToModel(u, model));

        RealEstate realEstate = realEstateService.findById(id);
        model.addAttribute("realEstate", realEstate);
        return "real-estate/info";
    }

    @GetMapping("/my")
    public String myRealEstates(Model model) {
        User user = getUserIfAuthenticated().orElseThrow();
        addUserAttributesToModel(user, model);

        List<RealEstate> realEstates = realEstateService.findByOwner(user);
        model.addAttribute("realEstates", realEstates);
        model.addAttribute("newRealEstate", new RealEstate());
        return "real-estate/user-estates";
    }

    @GetMapping("/edit/{id}")
    public String editRealEstate(@PathVariable("id") Long id, Model model) {
        User user = getUserIfAuthenticated().orElseThrow();
        addUserAttributesToModel(user, model);

        RealEstate realEstate = realEstateService.findById(id);
        if (realEstate.getOwner().getId().equals(user.getId())) {
            model.addAttribute("realEstate", realEstate);
            return "real-estate/edit";
        }
        return "redirect:/real-estate";
    }

    @PutMapping("/{id}")
    public String updateRealEstate(@PathVariable("id") Long id, @ModelAttribute("realEstate") RealEstate realEstate) {
        getUserIfAuthenticated().ifPresent(u -> {
            if (realEstateService.findById(id).getOwner().getId().equals(u.getId())) {
                realEstate.setId(id);
                realEstate.setOwner(u);
                realEstateService.save(realEstate);
            }
        });
        return "redirect:/real-estate/my";
    }

    @PostMapping
    public String create(@ModelAttribute("realEstate") RealEstate realEstate) {
        Optional<User> user = getUserIfAuthenticated();
        if (user.isPresent()) {
            realEstate.setOwner(user.get());
            realEstateService.save(realEstate);
            return "redirect:/real-estate/my";
        }
        return "redirect:/real-estate";
    }

    @DeleteMapping("/{id}")
    public @ResponseBody void delete(@PathVariable("id") Long id) {
        getUserIfAuthenticated().ifPresent(u -> {
            if (realEstateService.findById(id).getOwner().getId().equals(u.getId())) {
                realEstateService.deleteById(id);
            }
        });
    }

    private Optional<User> getUserIfAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (!principal.equals("anonymousUser")) {
                return Optional.of((User) principal);
            }
        }
        return Optional.empty();
    }

    private void addUserAttributesToModel(User user, Model model) {
        model.addAttribute("user", user);
        if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            model.addAttribute("isAdmin", true);
        }
    }

}
