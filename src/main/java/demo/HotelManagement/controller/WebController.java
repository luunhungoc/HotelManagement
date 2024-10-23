package demo.HotelManagement.controller;

import demo.HotelManagement.entities.RoomType;
import demo.HotelManagement.repository.RoomTypeRepository;
import demo.HotelManagement.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class WebController {
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private RoomTypeService roomTypeService;

    @RequestMapping("/")
    public String showHomePage(Model model) {
        List<RoomType> roomTypeList =  new ArrayList<>((Collection<RoomType>) roomTypeRepository.findAll());
        model.addAttribute("roomTypeList", roomTypeList);
        return "website/home";
    }

    @RequestMapping("/rooms")
    public String showRoomType(Model model) {
        List<RoomType> roomTypeList =  new ArrayList<>((Collection<RoomType>) roomTypeRepository.findAll());
        model.addAttribute("roomTypeList", roomTypeList);
        return "website/rooms";
    }

    @RequestMapping("/reservations")
    public String showReservations(Model model) {

        return "website/reservations";
    }

    @RequestMapping("/changePassword")
    public String changePassword() {

        return "profile/changePassword";
    }

    @RequestMapping("/profile/profile-edit")
    public String editProfile() {
        return "profile/profile-edit";
    }

    @RequestMapping("/contact")
    public String contact() {
        return "website/contact";
    }

}