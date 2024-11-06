package demo.HotelManagement.controller;

import demo.HotelManagement.entities.Profile;
import demo.HotelManagement.repository.ProfileRepository;
import demo.HotelManagement.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/registration")
public class LoginController {
    private static final String REGISTRATION = "Registration";

    @Autowired
    private EmailService emailService;

    @Autowired
    private ProfileRepository profileRepository;

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("profile", new Profile());
        return "website/loginAndRegister";
    }

    @PostMapping("/save")
    public String successRegistration(@ModelAttribute Profile profile) throws IOException {

        profileRepository.save(profile);

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("loginName", profile.getLoginName());
        templateModel.put("loginPassword", profile.getLoginPassword());
        templateModel.put("firstName", profile.getFirstName());
        templateModel.put("lastName", profile.getLastName());
        emailService.sendHtmlEmail(profile.getLoginName(), REGISTRATION, "website/mail_success_registration.html", templateModel);//define mail template and define hashmap to send value into template
        return "website/success_registration";
    }
}

