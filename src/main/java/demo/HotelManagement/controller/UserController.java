package demo.HotelManagement.controller;

import demo.HotelManagement.entities.Profile;

import demo.HotelManagement.repository.ProfileRepository;
import demo.HotelManagement.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private RoomTypeService roomTypeService;

    @RequestMapping("/")
    public String showHomePage(Model model) {
        List<Profile> profileList =  new ArrayList<>((Collection<Profile>) profileRepository.findAll());
        model.addAttribute("profileList", profileList);
        return "website/profile/profile-edit";
    }
}
