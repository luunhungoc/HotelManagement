package demo.HotelManagement.controller;
import demo.HotelManagement.entities.Room;
import demo.HotelManagement.entities.RoomType;
import demo.HotelManagement.entities.RoomsCreationDto;
import demo.HotelManagement.service.RoomService;
import demo.HotelManagement.service.RoomTypeService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomTypeService roomTypeService;

//    @GetMapping(value = "/room-list")
//    public String showAll(Model model) {
//        model.addAttribute("roomList", roomService.findAll());
//
//        return "admin/room/room-list";
//    }
//
//    @GetMapping(value = "/add-room")
//    public String showCreateForm(Model model) {
//        RoomsCreationDto roomsForm = new RoomsCreationDto();
//            roomsForm.addRoom(new Room());
//
//        model.addAttribute("form", roomsForm);
//
//        return "admin/room/add-room";
//    }
//
//    @GetMapping(value = "/edit")
//    public String showEditForm(Model model) {
//        List<Room> rooms = new ArrayList<>();
//        roomService.findAll()
//                .iterator()
//                .forEachRemaining(rooms::add);
//        List<RoomType> roomTypeList = roomTypeService.findAll();
//        model.addAttribute("form", new RoomsCreationDto(rooms));
//        model.addAttribute("roomTypeList", roomTypeList);
//        return "admin/room/edit-room";
//    }
//
//    @PostMapping(value = "/save")
//    public String saveRooms(@ModelAttribute RoomsCreationDto form, Model model) {
//        roomService.saveAll(form.getRooms());
//
//        model.addAttribute("rooms", roomService.findAll());
//
//        return "redirect:/room/room-list";
//    }

    /////
    // To fetch the initial data and display on the page
    // Hiển thị trang quản lý phòng
//    @GetMapping("/room-management")
//    public String showRoomManagement(Model model) {
//        model.addAttribute("roomList", roomService.findAll());
//        model.addAttribute("roomTypeList", roomTypeService.findAll());
//        return "admin/room/room-management-list"; // Thymeleaf template
//    }
//    @PostMapping("/add")
//    @ResponseBody
//    public ResponseEntity<?> addRoom(@RequestParam int number, @RequestParam Long roomTypeId) {
//        RoomType roomType = roomTypeService.findById(roomTypeId);
//        Room newRoom = new Room();
//        newRoom.setNumber(number);
//        newRoom.setRoomType(roomType);
//        roomService.save(newRoom);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/delete")
//    @ResponseBody
//    public ResponseEntity<?> deleteRoom(@RequestParam("roomId") Long roomId) {
//        Room room = roomService.findById(roomId);
//        if (room != null) {
//            roomService.deleteById(roomId);
//            return ResponseEntity.ok().build();  // Successfully deleted
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found");
//    }
//    @PostMapping("/add-room-type")
//    @ResponseBody
//    public ResponseEntity<?> addRoomType(@RequestParam String code, @RequestParam String name,
//                                         @RequestParam int maxAdult, @RequestParam int maxChild,
//                                         @RequestParam int maxOccupancy,
//                                         @RequestParam double price, @RequestParam double discount,
//                                         @RequestParam int quantity) {
//        RoomType roomType = new RoomType(code, name, maxAdult, maxChild, maxOccupancy, price, discount, quantity);
//        roomType.setCode(code);
//        roomType.setName(name);
//        roomType.setMaxAdult(maxAdult);
//        roomType.setMaxChild(maxChild);
//        roomType.setMaxOccupancy(maxOccupancy);
//        roomType.setPrice(price);
//        roomType.setDiscount(discount);
//        roomType.setQuantity(quantity);
//        roomTypeService.save(roomType);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/delete-room-type")
//    @ResponseBody
//    public ResponseEntity<?> deleteRoomType(@RequestParam("roomTypeId") Long roomTypeId) {
//        RoomType roomType = roomTypeService.findById(roomTypeId);
//        if (roomType != null) {
//            roomTypeService.deleteById(roomTypeId);
//            return ResponseEntity.ok().build();  // Successfully deleted
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room Type not found");
//    }
//
//    @PostMapping("/edit-room-type")
//    @ResponseBody
//    public ResponseEntity<String> editRoomType(@RequestParam Long roomTypeId,
//                                               @RequestParam String code, @RequestParam String name,
//                                               @RequestParam int maxAdult, @RequestParam int maxChild,
//                                               @RequestParam int maxOccupancy,
//                                               @RequestParam double price, @RequestParam double discount,
//                                               @RequestParam int quantity) {
//        RoomType roomType = roomTypeService.findById(roomTypeId);
//
//        if (roomType != null) {
//            roomType.setCode(code);
//            roomType.setName(name);
//            roomType.setMaxAdult(maxAdult);
//            roomType.setMaxChild(maxChild);
//            roomType.setMaxOccupancy(maxOccupancy);
//            roomType.setPrice(price);
//            roomType.setDiscount(discount);
//            roomType.setQuantity(quantity);
//            roomTypeService.save(roomType);  // Save the updated entity
//            return ResponseEntity.ok("Room type updated successfully");
//        }
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room type not found");
//    }
//
//    @GetMapping(value = "/search")
//    public String searchRoom(@RequestParam("searchRoomInput") String searchRoomInput, Model model) {
//        List<Room> rooms;
//
//        try {
//            // Kiểm tra nếu input là số, tìm kiếm theo Room.number
//            int number = Integer.parseInt(searchRoomInput);
//            rooms = roomService.getRoomsByCodeOrNumber("", number); // tìm theo number
//        } catch (NumberFormatException e) {
//            // Nếu không phải số, tìm kiếm theo RoomType.code
//            rooms = roomService.getRoomsByCodeOrNumber(searchRoomInput, 0); // tìm theo RoomType.code
//        }
//
//        model.addAttribute("roomList", rooms);
//        model.addAttribute("roomTypeList", roomTypeService.findAll());
//        return "admin/room/room-management-list";
//    }

}