package demo.HotelManagement.controller;

import demo.HotelManagement.entities.CentralReservation;
import demo.HotelManagement.entities.Profile;
import demo.HotelManagement.entities.Reservation;
import demo.HotelManagement.repository.*;
import demo.HotelManagement.service.CartService;
import demo.HotelManagement.session.CartSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.time.format.DateTimeFormatter;
@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    RoomTypeRepository roomTypeRepository;

    @Autowired
    CentralReservationRepository centralReservationRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    CartService cartService;

    @GetMapping
    public String getCart(Model model, CartSession cart) {
        List<CartSession> cartList = cartService.getCart();
        // display check in, check out date with format "dd MMM yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        for (CartSession item : cartList) {
            String formattedCheckInDate =item.getCheckInDate().format(formatter);
            String formattedCheckOutDate =item.getCheckOutDate().format(formatter);
            // Bạn có thể thêm checkInDate vào model hoặc xử lý nó theo cách bạn cần
            model.addAttribute("formattedCheckInDate", formattedCheckInDate);
            model.addAttribute("formattedCheckOutDate", formattedCheckOutDate);
        }

        model.addAttribute("profile",new Profile());
        model.addAttribute("cartList", cartList);
        return "website/cart";
    }

    @GetMapping("/other-service")
    public String getOtherService(Model model) {
        model.addAttribute("profile",new Profile());
        model.addAttribute("cartList", cartService.getCart());
        return "website/otherServiceCart";
    }

    @GetMapping("/complete-booking")
    public String getCompleteBooking(Model model) {
        model.addAttribute("profile",new Profile());
        model.addAttribute("cartList", cartService.getCart());
        return "website/complete-booking";
    }

    @PostMapping("/addToCart/{roomTypeId}")
    public String addToCart(@PathVariable Long roomTypeId,
                            Model model,
                            @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                            @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
                            @RequestParam("adultCount") Integer adults,
                            @RequestParam("childCount") Integer children) {

        cartService.addToCart(roomTypeId, checkInDate, checkOutDate, adults, children);

        return "redirect:/cart";
    }

    @PostMapping("/update/{reservationId}")
    public String updateCart(@PathVariable Long reservationId, @RequestParam Integer quantity) {
        cartService.updateCart(reservationId, quantity);
        return "redirect:/cart";
    }
    @GetMapping("/delete/{id}")
    public String deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return "redirect:/cart";
    }
//    @RequestMapping(value = "/addToCart/{id}")
//    public String addItem(@PathVariable("id") Long id, HttpSession session) {
//        // Retrieve the cart list from the session
//        List<CartSession> cartList = (List<CartSession>) session.getAttribute("cartList");
//
//        // If the cart list is null, initialize it and set it in the session
//        if (cartList == null) {
//            cartList = new ArrayList<>();
//            session.setAttribute("cartList", cartList);
//        }
//
//        // Retrieve the RoomType using Optional to avoid NoSuchElementException
//        Optional<RoomType> optionalRoomType = roomTypeRepository.findById(id);
//        if (optionalRoomType.isPresent()) {  // Check if the RoomType exists
//            RoomType roomType = optionalRoomType.get();  // Get the RoomType object
//
//            boolean itemExists = false;
//
//            // Check if the item is already in the cart
//            for (CartSession item : cartList) {
//                if (item.getRoomType().getId().equals(id)) {  // Use equals for object comparison
//                    item.setQuantity(item.getQuantity() + 1);  // Increment the quantity
//                    itemExists = true;
//                    break;
//                }
//            }
//
//            // If the item doesn't exist in the cart, add it
//            if (!itemExists) {
//                cartList.add(new CartSession(roomType, 1));  // Add new item with quantity 1
//            }
//        } else {
//            // Log the error or handle it if RoomType is not found
//            System.out.println("RoomType with id " + id + " not found!");
//        }
//        System.out.println("Cart List: " + cartList);
//        return "website/cart";  // Return the view name
//    }

//    @RequestMapping(value = "/removeItem/{id}")
//    public String removeItem(@PathVariable Long id, HttpSession session) {
//        List<CartSession> cartList = (List<CartSession>) session.getAttribute("cartList");
//        if (cartList != null) {
//            cartList.removeIf(item -> item.getRoomType().getId() == id);
//        }
//        return "website/democart/cart";
//
//    }
//
//    @RequestMapping(value = "/placeOrder", method = RequestMethod.POST)
//    public String placeOrder(Profile profile, HttpSession session) {
//        // Retrieve the cart items from the session
//        List<CartSession> cartList = (List<CartSession>) session.getAttribute("cartList");
//
//        // Step 1: Save the new Profile entity to the database
//        profileRepository.save(profile);
//
//        // Step 2: Create and set up a new CentralReservation
//        CentralReservation centralReservation = new CentralReservation();
//        centralReservation.setProfile(profile);  // Associate the profile with the central reservation
//        centralReservation.setBookingDate(LocalDate.now());
//
//        // Step 3: Save CentralReservation before adding the reservations
//        centralReservationRepository.save(centralReservation);
//
//        // Step 4: Create and save each reservation linked to CentralReservation and Profile
//        for (CartSession item : cartList) {
//            Reservation reservation = new Reservation();
//            reservation.setRoomType(item.getRoomType());
//            reservation.setQuantity(item.getQuantity());
//            reservation.setMarket("ONL");
//            reservation.setRoomToCharge(item.getRoomType().getCode());
//            reservation.setCentralReservation(centralReservation);  // Link to CentralReservation
//            reservation.setProfile(profile);
//            reservation.setCheckInDate(item.getCheckInDate());
//
//            reservationRepository.save(reservation);
//        }
//
//        // Step 5: Clear the cart after placing the order
//        session.removeAttribute("cartList");
//
//        return "redirect:/centralReservations";
//    }
//
//
//
//    @RequestMapping(value = "/checkOut")
//    public String showCheckOutForm(Model model) {
//        model.addAttribute("centralReservations",new CentralReservation());
//        model.addAttribute("profile",new Profile());
//        return "website/democart/checkout";
//    }
//
//
//    @RequestMapping(value = "/centralReservations")
//    public String showBookings(Model model, HttpServletRequest request) {
//        List<CentralReservation> centralReservations = (List<CentralReservation>) centralReservationRepository.findAll();
//        model.addAttribute("centralReservations", centralReservations);
//        return "website/democart/orders";
//    }



}
