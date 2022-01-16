package app.controllers;

import app.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LogInController {

    @Autowired
    private SecurityService securityService;

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (securityService.isAuthenticated())
            return "redirect:/";
        if (error != null)
            model.addAttribute("error", "username and password is invalid");
        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully");
        return "login";
    }
}
