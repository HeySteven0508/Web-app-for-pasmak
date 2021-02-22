package com.pasmakms.demo.controller;


import com.pasmakms.demo.domain.RegistrationForm;
import com.pasmakms.demo.repositories.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private UserAccountRepository userRepo;
    private PasswordEncoder passwordEncoder;

    public RegistrationController(UserAccountRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showRegistrationPage(Model model){
        model.addAttribute("RegistrationForm",new RegistrationForm());
        return "registration";
    }

    @PostMapping
    public String processRegistration(@ModelAttribute RegistrationForm form){
        form.setRoles("ADMIN");
        userRepo.save(form.toUserAccount(passwordEncoder));
        return "redirect:/login";
    }
}
