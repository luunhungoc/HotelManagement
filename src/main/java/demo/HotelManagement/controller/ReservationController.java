package demo.HotelManagement.controller;

import demo.HotelManagement.entities.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
//    @GetMapping("/other-service")
//    public String getOtherService(Model model) {
//        model.addAttribute("profile",new Profile());
//        model.addAttribute("cartList", cartService.getCart());
//        return "website/otherServiceCart";
//    }
}
