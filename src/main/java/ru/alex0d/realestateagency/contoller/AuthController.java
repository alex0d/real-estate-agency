package ru.alex0d.realestateagency.contoller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.alex0d.realestateagency.dto.SignUpDto;
import ru.alex0d.realestateagency.service.UserService;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("signUpDto", new SignUpDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("signUpDto") SignUpDto signUpDto,
                               BindingResult bindingResult,
                               HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "register";
        } else if (!signUpDto.getPassword().equals(signUpDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.user", "Passwords do not match");
            return "register";
        } else if (userService.findByUsername(signUpDto.getUsername()) != null) {
            bindingResult.rejectValue("username", "error.user", "Username already exists");
            return "register";
        } else if (userService.findByEmail(signUpDto.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.user", "Email already exists");
            return "register";
        } else {
            userService.registerUser(signUpDto);
            try {
                request.login(signUpDto.getUsername(), signUpDto.getPassword());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "redirect:/";
        }
    }
}
