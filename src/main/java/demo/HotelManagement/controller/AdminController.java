package demo.HotelManagement.controller;

import demo.HotelManagement.entities.*;
import demo.HotelManagement.repository.MembershipRepository;
import demo.HotelManagement.service.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.thymeleaf.util.StringUtils.isEmpty;

@Controller
@RequestMapping("/admin")
public class AdminController {
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

    @GetMapping("/registration-card/{id}")
    public String showRegistrationCard(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.findById(id);

        model.addAttribute("res", reservation);
        model.addAttribute("roomList", roomService.findAll());
        model.addAttribute("roomTypeList", roomTypeService.findAll());
        return "admin/reservation/registration-card";
    }

    @GetMapping("/folio/{id}")
    public String showFolio(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.findById(id);
        List<Transaction> transactions = transactionService.getTransactionsByReservationId(id);
        // Tính toán subtotal cho từng transaction và lưu vào danh sách subtotals
        List<Double> subtotals = new ArrayList<>();
        double totalCharges = 0.0;
        double totalPayment = 0.0;
        for (Transaction transaction : transactions) {
            double subtotal = transaction.getQuantity() * transaction.getAmount();
            subtotals.add(subtotal);

            // Cộng dồn vào totalCharges nếu subtotal > 0, ngược lại cộng vào totalPayment
            if (subtotal > 0) {
                totalCharges += subtotal;
            } else if (subtotal < 0) {
                totalPayment += subtotal;
            }
        }

        // Tổng số dư khách hàng (có thể tính tổng subtotal)
        double guestBalance = totalCharges + totalPayment;

        model.addAttribute("guestBalance", guestBalance);
        model.addAttribute("totalCharges", totalCharges);
        model.addAttribute("totalPayment", totalPayment);
        model.addAttribute("transactions", transactions);
        model.addAttribute("res", reservation);
        model.addAttribute("roomList", roomService.findAll());
        model.addAttribute("roomTypeList", roomTypeService.findAll());
        return "admin/reservation/folio";
    }

    @GetMapping("/room-assignment")
    public String showRoomAssignment(Model model) {
        List<Room> rooms = roomService.findRoomsByDate(LocalDate.now());
        model.addAttribute("rooms", rooms);
        return "admin/room/room-assignment";
    }

    @GetMapping("/room-assignment/search")
    public String showRoomAssignmentSearch(@RequestParam("searchDate") LocalDate searchDate, Model model) {

        List<Room> rooms = roomService.findRoomsByDate(searchDate);
        model.addAttribute("rooms", rooms);
        return "admin/room/room-assignment";
    }

    // Hiển thị trang quản lý phòng
    @GetMapping("/room/room-type-management")
    public String showRoomManagement(Model model) {
        model.addAttribute("roomList", roomService.findAll());
        model.addAttribute("roomTypeList", roomTypeService.findAll());
        return "admin/room/room-type-list"; // Thymeleaf template
    }

    public double getDiscountedPriceForRoomType(RoomType roomType) {
        List<Discount> applicableDiscounts = discountService.getApplicableDiscounts();
        return roomType.calculateDiscountedPrice(applicableDiscounts);
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
    public ResponseEntity<String> editRoomType(@RequestParam("roomTypeId") Long roomTypeId,
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

    @GetMapping("/room/search-room-type")
    public String searchRoomType(@RequestParam("searchInput") String searchInput, Model model) {
        List<RoomType> roomTypes = roomTypeService.getRoomTypeByCodeOrName(searchInput,searchInput);

        model.addAttribute("roomTypeList", roomTypes);
        model.addAttribute("roomList", roomService.findAll());
        return "admin/room/room-type-list";
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

    @PostMapping("/room/upload-room-type-photo")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("image") MultipartFile file,
            @RequestParam("id") Long roomTypeId) throws IOException {

        Map<String, Object> response = new HashMap<>();

        if (file.isEmpty()) {
            response.put("success", false);
            response.put("message", "No file selected");
            return ResponseEntity.badRequest().body(response);
        }
        // Lấy đường dẫn đến thư mục static/img
        File saveFile = new ClassPathResource("static/img").getFile();
        System.out.println(saveFile);

        // Tạo thư mục room-type nếu chưa tồn tại
        File roomTypeDir = new File(saveFile, "room-type");
        if (!roomTypeDir.exists()) {
            roomTypeDir.mkdirs();
        }

        // Tạo đường dẫn cho file
        Path path = Paths.get(roomTypeDir.getAbsolutePath(), file.getOriginalFilename());
        System.out.println(path);

        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "File upload failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        // Cập nhật đường dẫn của roomPhoto trong database
        String roomPhoto = "/img/room-type/" + file.getOriginalFilename();
        roomTypeService.updateImageUrl(roomTypeId, roomPhoto);

        response.put("success", true);
        response.put("roomPhoto", roomPhoto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public String showUserList(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "7") int size,
                                  Model model)  {
        Page<Profile> profiles = profileService.getProfiles(page, size);

        model.addAttribute("profiles", profiles.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", profiles.getTotalPages());

        return "admin/profile/profile-list"; // Thymeleaf template
    }

    @GetMapping("/user/{id}")
    public String showEditProfile(@PathVariable Long id, Model model) {
        Profile profile = profileService.findById(id);
        if (profile.getAvatar() == null || profile.getAvatar().isEmpty()) {
            profile.setAvatar("/img/profile/avatar-default.jpg");
        }
        List<Reservation> reservations = reservationService.findByProfileId(id);
        Map<Long, Long> roomRevenues = new HashMap<>();
        Map<Long, Long> otherRevenues = new HashMap<>();
        Long totalRoomRevenue = 0L;
        Long totalOtherRevenue = 0L;
        for (Reservation res : reservations) {
            Long roomRevenue= transactionService.getTotalRoomRevenueByReservationId(res.getId());
            Long otherRevenue= transactionService.getTotalOtherRevenueByReservationId(res.getId());
            roomRevenues.put(res.getId(), roomRevenue != null ? roomRevenue : 0L);
            otherRevenues.put(res.getId(), otherRevenue != null ? otherRevenue : 0L);
            // Tính tổng doanh thu
            totalRoomRevenue += roomRevenues.get(res.getId());
            totalOtherRevenue += otherRevenues.get(res.getId());
        }

        model.addAttribute("totalRoomRevenue", totalRoomRevenue);
        model.addAttribute("totalOtherRevenue", totalOtherRevenue);
        model.addAttribute("roomRevenues", roomRevenues);
        model.addAttribute("otherRevenues", otherRevenues);
        model.addAttribute("profile", profile);
        model.addAttribute("reservations", reservations);
        return "admin/profile/profile-edit";
    }

    @PostMapping("/upload-profile-photo/{id}")
    public String uploadProfilePhoto(@RequestParam("image") MultipartFile file,
                                     @PathVariable Long id,
                                     RedirectAttributes redirectAttributes) throws IOException {

        try { if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/admin/user/" + id;
        }

            File saveFile = new ClassPathResource("static/img").getFile();
            File profileDir = new File(saveFile, "profile");
            if (!profileDir.exists()) {
                profileDir.mkdirs(); // Tạo thư mục nếu chưa tồn tại
            }

            Path path = Paths.get(profileDir.getAbsolutePath(), file.getOriginalFilename());

            // Lưu file vào thư mục
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Cập nhật đường dẫn avatar trong database
            String profileUrl = "/img/profile/" + file.getOriginalFilename();
            profileService.updateImageUrl(id, profileUrl);

            redirectAttributes.addFlashAttribute("message", "Profile picture uploaded successfully.");

        }catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Failed to upload profile picture.");
        }
        return "redirect:/admin/user/" + id;
    }

}

