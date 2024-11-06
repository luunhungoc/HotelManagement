package demo.HotelManagement.controller;

import demo.HotelManagement.entities.*;
import demo.HotelManagement.repository.MembershipRepository;
import demo.HotelManagement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/admin/discount")
public class DiscountController {

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private DiscountService discountService;

    // Show the discount list and the form
    @GetMapping("/list")
    public String listDiscounts(Model model) {
        discountService.updateDiscountStatus();
        model.addAttribute("discount", new Discount());  // Add empty discount object for form binding
        model.addAttribute("roomTypes", roomTypeService.findAll());
        model.addAttribute("discounts", discountService.getAllDiscounts());  // Add list of discounts
        return "admin/reservation/discount-list";
    }

    // Handle form submission to add a new discount
    @PostMapping("/add")
    public String addDiscount(@ModelAttribute("discount") Discount discount, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // If there are errors, reload the form with validation errors
            List<Discount> discounts = discountService.getAllDiscounts();
            model.addAttribute("roomTypes", roomTypeService.findAll());
            model.addAttribute("discounts", discounts);
            return "admin/reservation/discount-list";
        }

        // Save the discount
        discountService.saveDiscount(discount);

        return "redirect:/admin/discount/list";  // Redirect to avoid form resubmission on page refresh
    }


    // Handle DELETE request to remove a discount
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public String showDiscountSearch(@RequestParam("searchInput") String searchInput, Model model) {

        List<Discount> discount = discountService.findByCode(searchInput);
        model.addAttribute("discount",discount);
        return "admin/reservation/discount-list";
    }
}

