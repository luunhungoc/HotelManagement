package demo.HotelManagement.controller;

import demo.HotelManagement.entities.AvailableRoomInfo;
import demo.HotelManagement.entities.RoomType;
import demo.HotelManagement.repository.RoomTypeRepository;
import demo.HotelManagement.service.RoomService;
import demo.HotelManagement.service.RoomTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;


@Controller
@RequestMapping
public class RoomTypeController {
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private RoomService roomService;

    @GetMapping("/search-availability")
    public String getRoomAvailability(@RequestParam("checkInDate") String checkInDate,
                                      @RequestParam("checkOutDate") String checkOutDate,
                                      @RequestParam(value = "adultCount", required = false) Integer adultCount,
                                      @RequestParam(value = "childCount", required = false) Integer childCount,
                                      @RequestParam(value = "roomTypeName", required = false) String roomTypeName,
                                      Model model) {

        LocalDate checkIn = LocalDate.parse(checkInDate);
        LocalDate checkOut = LocalDate.parse(checkOutDate);
        // Tạo StringBuilder để nhận thông báo từ Service
        StringBuilder roomFullMessages = new StringBuilder();

        List<AvailableRoomInfo> availableRooms = roomTypeService.findAvailableRooms(roomFullMessages,checkIn, checkOut, adultCount, childCount,roomTypeName);
        List<RoomType> roomTypeList =  new ArrayList<>((Collection<RoomType>) roomTypeRepository.findAll());
        model.addAttribute("roomTypeList", roomTypeList);
        model.addAttribute("availableRooms", availableRooms);
        model.addAttribute("checkInDate", checkInDate);
        model.addAttribute("checkOutDate", checkOutDate);
        model.addAttribute("roomFullMessages", roomFullMessages.toString());
        System.out.println("Available Rooms: " + availableRooms);
        return "website/rooms-available";
    }

    @GetMapping("/room-availability-by-date")
    public String getRoomAvailability(@RequestParam("checkInDate") String checkInDate,
                                      Model model) {

        LocalDate checkIn = LocalDate.parse(checkInDate);

        // Tính toán danh sách các ngày trong khoảng từ check-in đến 14 ngày sau
        List<LocalDate> dateRange = new ArrayList<>();
        for (LocalDate date = checkIn; !date.isAfter(checkIn.plusDays(14)); date = date.plusDays(1)) {
            dateRange.add(date);
        }

        // Lấy danh sách phòng trống theo ngày và loại phòng
        Map<String, Map<LocalDate, Integer>> availableRoomsByDate = roomTypeService.findAvailableRoomsByDate(checkIn);

        // Thêm dữ liệu vào model để hiển thị trên giao diện
        model.addAttribute("dateRange", dateRange);
        model.addAttribute("availableRoomsByDate", availableRoomsByDate);
        model.addAttribute("checkInDate", checkInDate);
        return "report/room-availability-by-date";
    }


}
