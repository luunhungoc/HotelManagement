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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin/reservation")
public class ReservationController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionTypeService transactionTypeService;

    @Autowired
    private MembershipRepository membershipRepository;

    @GetMapping
    public String showReservation(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  Model model)  {
        Page<Reservation> reservationPage = reservationService.getReservations(page, size);

        model.addAttribute("reservations", reservationPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reservationPage.getTotalPages());
        model.addAttribute("isSearching", false); // Thêm cờ để đánh dấu không phải tìm kiếm
        return "admin/reservation/reservation-list"; // Thymeleaf template
    }

    @GetMapping("/search")
    public String searchReservations(@RequestParam(required = false) String lastName,
                                     @RequestParam(required = false) String firstName,
                                     @RequestParam(required = false) Long reservationId,
                                     @RequestParam(required = false) LocalDate arrivalFrom,
                                     @RequestParam(required = false) LocalDate arrivalTo,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size,
                                     Model model) {

        Page<Reservation> reservationPage = reservationService.searchReservations(lastName, firstName, reservationId, arrivalFrom, arrivalTo, page, size);

        model.addAttribute("reservations", reservationPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reservationPage.getTotalPages());
        model.addAttribute("isSearching", true); // Mark as a search

        model.addAttribute("firstName", firstName != null ? firstName : "");
        model.addAttribute("lastName", lastName != null ? lastName : "");
        model.addAttribute("reservationId", reservationId);
        model.addAttribute("arrivalFrom", arrivalFrom);
        model.addAttribute("arrivalTo", arrivalTo);

        return "admin/reservation/reservation-list";
    }

    @GetMapping("/add")
    public String addReservationForm(Model model) {
        Reservation reservation = new Reservation();
        reservation.setAdult(2);
        reservation.setQuantity(1);
        model.addAttribute("res",reservation);
        model.addAttribute("profile", new Profile());
        model.addAttribute("resStatus", ReservationStatus.values());
        model.addAttribute("roomList", roomService.findAll());
        model.addAttribute("roomTypeList", roomTypeService.findAll());
        model.addAttribute("discounts",discountService.getAllDiscounts());
        return "admin/reservation/new-reservation";
    }

    @PostMapping("/add")
    public String createReservation(@ModelAttribute("res") Reservation reservation,
                                    Model model) {
        Profile profile= reservation.getProfile();
        Discount discount=reservation.getDiscount();
        if (profile.getMembership() == null || profile.getMembership().getLevel().isEmpty()) {
            profile.setMembership(null);
        } else {
            Long membershipId = profile.getMembership().getId();
            Membership existingMembership = membershipRepository.findById(membershipId).orElse(null);

            if (existingMembership == null) {
                throw new IllegalArgumentException("Membership not found");
            }
            profile.setMembership(existingMembership);
        }
        if (reservation.getDiscount() == null || reservation.getDiscount().getPercentage()==0) {
            reservation.setDiscount(null);
        }
        if (reservation.getRoom().getId() == null) {
            reservation.setRoom(null);
        }
        reservation.setQuantity(reservation.getQuantity());

        reservation.setStatus(ReservationStatus.NON_GUARANTEED);
        reservation.setMarket("WLK");
        reservation.setCentralReservation(null);

        reservation.setProfile(profileService.save(profile));
        reservationService.save(reservation);
        model.addAttribute("message", "Reservation created successfully! Booking #" + reservation.getId());

        return "admin/reservation/new-reservation";
    }

    // Phương thức tính guestBalance có thể được định nghĩa như sau
    private double calculateGuestBalance(Long reservationId) {
        List<Transaction> transactions = transactionService.getTransactionsByReservationId(reservationId);
        double totalSubtotal = 0.0;

        for (Transaction transaction : transactions) {
            totalSubtotal += transaction.getQuantity() * transaction.getAmount();
        }

        return totalSubtotal;
    }

    @GetMapping("/edit/{id}")
    public String showReservationDetail(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.findById(id);
        List<Transaction> transactions = transactionService.getTransactionsByReservationId(id);

        // Tính toán subtotal cho từng transaction và lưu vào danh sách subtotals
        List<Double> subtotals = new ArrayList<>();
        double totalSubtotal = 0.0;
        for (Transaction transaction : transactions) {
            double subtotal = transaction.getQuantity() * transaction.getAmount();
            subtotals.add(subtotal);
            totalSubtotal += subtotal;
        }

        model.addAttribute("subtotals", subtotals);
        model.addAttribute("guestBalance", totalSubtotal);
        model.addAttribute("res", reservation);
        model.addAttribute("checkedInStatus", ReservationStatus.CHECKED_IN);
        model.addAttribute("roomList", roomService.findAll());
        model.addAttribute("roomTypeList", roomTypeService.findAll());
        model.addAttribute("discounts",discountService.getAllDiscounts());
        model.addAttribute("transactionList", transactions);
        model.addAttribute("transactionTypes", transactionTypeService.findAll());
        model.addAttribute("transactionUnits", TransactionTypeUnit.values());
        return "admin/reservation/reservation-detail";
    }

    @PostMapping("/edit/{id}")
    public String editReservationDetail(@PathVariable Long id, @ModelAttribute Reservation res,
                                        RedirectAttributes redirectAttributes, Model model) {
        Reservation existingRes = reservationService.findById(id);

        if (res.getStatus() == ReservationStatus.CHECKED_OUT) {
            redirectAttributes.addFlashAttribute("error", "Unable to edit after check-out.");
            return "redirect:/reservation/edit/" + id;
        }

        // Cập nhật thông tin của Profile
        Profile existingProfile = existingRes.getProfile(); // Lấy Profile từ Reservation hiện có
        Profile updatedProfile = res.getProfile(); // Lấy Profile mới từ dữ liệu người dùng

        existingProfile.setFirstName(updatedProfile.getFirstName());
        existingProfile.setLastName(updatedProfile.getLastName());
        existingProfile.setPhone(updatedProfile.getPhone());
        existingProfile.setLoginName(updatedProfile.getLoginName());
        existingProfile.setBillingName(updatedProfile.getBillingName());
        if (existingProfile.getMembership() == null) {
            Membership newMembership = new Membership();
            newMembership.setLevel(updatedProfile.getMembership().getLevel()); // Set necessary properties
            membershipRepository.save(newMembership);

            existingProfile.setMembership(newMembership); // Assign new membership to profile
        } else {
            Long membershipId = existingProfile.getMembership().getId();
            Membership existingMembership = membershipRepository.findById(membershipId).orElse(null);

            if (existingMembership == null) {
                throw new IllegalArgumentException("Membership not found");
            }

            existingProfile.setMembership(existingMembership);
        }

        profileService.save(existingProfile); // Lưu Profile đã cập nhật

        // Cập nhật thông tin của Reservation
        existingRes.setProfile(existingProfile);
        existingRes.setRoomType(res.getRoomType());
        existingRes.setRoomTypeCurrent(res.getRoomTypeCurrent());
        existingRes.setRoom(res.getRoom());
        existingRes.setDiscount(res.getDiscount());
        existingRes.setCheckInDate(res.getCheckInDate());
        existingRes.setCheckOutDate(res.getCheckOutDate());
        existingRes.setNights((int) ChronoUnit.DAYS.between(res.getCheckInDate(), res.getCheckOutDate()));
        existingRes.setMarket(res.getMarket());
        existingRes.setAdult(res.getAdult());
        existingRes.setChild(res.getChild());

        // Cập nhật các trường khác nếu có

        reservationService.save(existingRes); // Lưu Reservation đã cập nhật
        redirectAttributes.addFlashAttribute("message", "Reservation #"+ res.getId()+" updated successfully!");

        return "redirect:/admin/reservation/edit/{id}"; // Điều hướng sau khi lưu thành công
    }

    // Check-in: cập nhật trạng thái và giờ check-in
    @PostMapping("/edit/{id}/check-in")
    public String checkIn(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Reservation reservation = reservationService.findById(id);
        if (reservation.getRoom() != null ) {
            reservation.setCheckInTime(LocalDateTime.now());
            reservation.setStatus(ReservationStatus.CHECKED_IN);
            reservationService.save(reservation);
            redirectAttributes.addFlashAttribute("message", "Check-in successfully!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Unable to check in. Please double check!");
        }
        return "redirect:/admin/reservation/edit/{id}";
    }

    @PostMapping("/edit/{id}/check-out")
    public String checkOut(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Reservation reservation = reservationService.findById(id);
        double guestBalance = calculateGuestBalance(reservation.getId());
        if (reservation.getStatus() == ReservationStatus.CHECKED_IN && guestBalance==0) {
            reservation.setCheckOutTime(LocalDateTime.now());
            reservation.setStatus(ReservationStatus.CHECKED_OUT);
            reservationService.save(reservation);
            redirectAttributes.addFlashAttribute("message", "Check-out successfully!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Unable to check out. Please check Reservation status or complete Guest Balance.");
        }
        return "redirect:/admin/reservation/edit/{id}";
    }

    @PostMapping("/edit/{id}/cancel")
    public String cancel(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Reservation reservation = reservationService.findById(id);
        if (reservation.getStatus() == ReservationStatus.NON_GUARANTEED) {
            reservation.setStatus(ReservationStatus.CANCELLED);
            reservationService.save(reservation);
            redirectAttributes.addFlashAttribute("message", "Cancel successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Unable to check out. Please check Reservation status.");
        }
        return "redirect:/admin/reservation/edit/{id}";
    }


    @GetMapping("/apply-discount")
    @ResponseBody
    public ResponseEntity<?> applyDiscount(@RequestParam Long discountId,
                                           @RequestParam Long reservationId,
                                           @RequestParam Long roomTypeId) {
        RoomType roomType = roomTypeService.findById(roomTypeId);
        Discount discount = discountService.findById(discountId);
        Reservation res=reservationService.findById(reservationId);

        if (roomType != null && discount != null) {
            double discountPercentage = discount.getPercentage();
            double newPrice = roomType.getPrice() * (1 - discountPercentage / 100);
            // Update the reservation with the discount and new price
            res.setDiscount(discount);
            reservationService.save(res);
            return ResponseEntity.ok(Collections.singletonMap("newPrice", newPrice));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid discount code or room type.");
    }
}
