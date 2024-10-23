package demo.HotelManagement.controller;

import demo.HotelManagement.entities.*;
import demo.HotelManagement.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionTypeService transactionTypeService;

    @GetMapping("/reservation")
    public String showReservation(Model model) {
        model.addAttribute("reservations", reservationService.findAll());
        return "admin/reservation/reservation-list"; // Thymeleaf template
    }

    @GetMapping("/reservation/edit/{id}")
    public String showReservationDetail(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.findById(id);
        List<Transaction> transactions = transactionService.getTransactionsByReservationId(id);

        model.addAttribute("res", reservation);
        model.addAttribute("roomList", roomService.findAll());
        model.addAttribute("roomTypeList", roomTypeService.findAll());
        model.addAttribute("transactionList", transactions);
        model.addAttribute("transactionTypes", transactionTypeService.findAll());
        model.addAttribute("transactionUnits", TransactionTypeUnit.values());
        return "admin/reservation/reservation-detail";
    }

    @PostMapping("/reservation/edit/{id}")
    public String editReservationDetail(@PathVariable Long id, @ModelAttribute Reservation res,
                                        RedirectAttributes redirectAttributes) {
        Reservation existingRes = reservationService.findById(id);

        // Cập nhật thông tin của Profile
        Profile existingProfile = existingRes.getProfile(); // Lấy Profile từ Reservation hiện có
        Profile updatedProfile = res.getProfile(); // Lấy Profile mới từ dữ liệu người dùng

        existingProfile.setFirstName(updatedProfile.getFirstName());
        existingProfile.setLastName(updatedProfile.getLastName());
        existingProfile.setPhone(updatedProfile.getPhone());
        existingProfile.setLoginName(updatedProfile.getLoginName());
        existingProfile.setBillingName(updatedProfile.getBillingName());
        existingProfile.setMembership(updatedProfile.getMembership());
        // Cập nhật các trường khác nếu có

        profileService.save(existingProfile); // Lưu Profile đã cập nhật

        // Cập nhật thông tin của Reservation
        existingRes.setProfile(existingProfile); // Gắn lại profile đã cập nhật
        existingRes.setCheckInDate(res.getCheckInDate());
        existingRes.setCheckOutDate(res.getCheckOutDate());
        existingRes.setMarket(res.getMarket());
        existingRes.setAdult(res.getAdult());
        existingRes.setChild(res.getChild());
        // Cập nhật các trường khác nếu có

        reservationService.save(existingRes); // Lưu Reservation đã cập nhật
        redirectAttributes.addFlashAttribute("success", "Reservation and Profile updated successfully!");
        return "redirect:/admin/reservation"; // Điều hướng sau khi lưu thành công
    }


    // Hiển thị trang quản lý phòng
    @GetMapping("/room/room-management")
    public String showRoomManagement(Model model) {
        model.addAttribute("roomList", roomService.findAll());
        model.addAttribute("roomTypeList", roomTypeService.findAll());
        return "admin/room/room-management-list"; // Thymeleaf template
    }
    @PostMapping("/room/add")
    @ResponseBody
    public ResponseEntity<?> addRoom(@RequestParam int number, @RequestParam Long roomTypeId) {
        RoomType roomType = roomTypeService.findById(roomTypeId);
        Room newRoom = new Room();
        newRoom.setNumber(number);
        newRoom.setRoomType(roomType);
        roomService.save(newRoom);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/room/delete")
    @ResponseBody
    public ResponseEntity<?> deleteRoom(@RequestParam("roomId") Long roomId) {
        Room room = roomService.findById(roomId);
        if (room != null) {
            roomService.deleteById(roomId);
            return ResponseEntity.ok().build();  // Successfully deleted
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found");
    }
    @PostMapping("/room/add-room-type")
    @ResponseBody
    public ResponseEntity<?> addRoomType(@RequestParam String code, @RequestParam String name,
                                         @RequestParam int maxAdult, @RequestParam int maxChild,
                                         @RequestParam int maxOccupancy,
                                         @RequestParam double price, @RequestParam double discount,
                                         @RequestParam int quantity) {
        RoomType roomType = new RoomType(code, name, maxAdult, maxChild, maxOccupancy, price, discount, quantity);
        roomType.setCode(code);
        roomType.setName(name);
        roomType.setMaxAdult(maxAdult);
        roomType.setMaxChild(maxChild);
        roomType.setMaxOccupancy(maxOccupancy);
        roomType.setPrice(price);
        roomType.setDiscount(discount);
        roomType.setQuantity(quantity);
        roomTypeService.save(roomType);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/room/delete-room-type")
    @ResponseBody
    public ResponseEntity<?> deleteRoomType(@RequestParam("roomTypeId") Long roomTypeId) {
        RoomType roomType = roomTypeService.findById(roomTypeId);
        if (roomType != null) {
            roomTypeService.deleteById(roomTypeId);
            return ResponseEntity.ok().build();  // Successfully deleted
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room Type not found");
    }

    @PostMapping("/room/edit-room")
    @ResponseBody
    public ResponseEntity<String> editRoom(@RequestParam Long roomId,
                                               @RequestParam int number, @RequestParam Long roomTypeId) {
        Room room = roomService.findById(roomId);
        RoomType roomType = roomTypeService.findById(roomTypeId);
        if (room != null) {
            room.setNumber(number);
            room.setRoomType(roomType);

            roomService.save(room);  // Save the updated entity
            return ResponseEntity.ok("Room updated successfully");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found");
    }

    @PostMapping("/room/edit-room-type")
    @ResponseBody
    public ResponseEntity<String> editRoomType(@RequestParam Long roomTypeId,
                                               @RequestParam String code, @RequestParam String name,
                                               @RequestParam int maxAdult, @RequestParam int maxChild,
                                               @RequestParam int maxOccupancy,
                                               @RequestParam double price, @RequestParam double discount,
                                               @RequestParam int quantity) {
        RoomType roomType = roomTypeService.findById(roomTypeId);

        if (roomType != null) {
            roomType.setCode(code);
            roomType.setName(name);
            roomType.setMaxAdult(maxAdult);
            roomType.setMaxChild(maxChild);
            roomType.setMaxOccupancy(maxOccupancy);
            roomType.setPrice(price);
            roomType.setDiscount(discount);
            roomType.setQuantity(quantity);
            roomTypeService.save(roomType);  // Save the updated entity
            return ResponseEntity.ok("Room type updated successfully");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room type not found");
    }

    @GetMapping(value = "/room/search")
    public String searchRoom(@RequestParam("searchRoomInput") String searchRoomInput, Model model) {
        List<Room> rooms;

        try {
            // Kiểm tra nếu input là số, tìm kiếm theo Room.number
            int number = Integer.parseInt(searchRoomInput);
            rooms = roomService.getRoomsByCodeOrNumber("", number); // tìm theo number
        } catch (NumberFormatException e) {
            // Nếu không phải số, tìm kiếm theo RoomType.code
            rooms = roomService.getRoomsByCodeOrNumber(searchRoomInput, 0); // tìm theo RoomType.code
        }

        model.addAttribute("roomList", rooms);
        model.addAttribute("roomTypeList", roomTypeService.findAll());
        return "admin/room/room-management-list";
    }

}

