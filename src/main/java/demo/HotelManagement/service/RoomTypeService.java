package demo.HotelManagement.service;

import demo.HotelManagement.entities.AvailableRoomInfo;
import demo.HotelManagement.entities.Room;
import demo.HotelManagement.entities.RoomType;
import demo.HotelManagement.repository.ReservationRepository;
import demo.HotelManagement.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class RoomTypeService {

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public List<RoomType> findAll() {
        return (List<RoomType>) roomTypeRepository.findAll();
    }

    public void saveAll(List<RoomType> roomTypes) {
        roomTypeRepository.saveAll(roomTypes);
    }

    public RoomType findById(Long id) {
        return roomTypeRepository.findById(id).orElse(null);
    }
    public void deleteById(Long id) {
        roomTypeRepository.deleteById(id);
    }
    public RoomType save(RoomType roomType) {
        return roomTypeRepository.save(roomType);
    }

    public List<RoomType> getRoomTypeByCodeOrName(String code, String name) {
        return roomTypeRepository.findByCodeContainingOrNameContaining(code,name);
    }

    /**
     * Tìm kiếm số lượng phòng trống tối thiểu theo ngày check-in và check-out, so nguoi lon, roomType
     */
    public List<AvailableRoomInfo> findAvailableRooms(StringBuilder roomFullMessages,LocalDate checkInDate, LocalDate checkOutDate, Integer adultCount, Integer childCount, String roomTypeName) {
        List<AvailableRoomInfo> availableRoomInfoList = new ArrayList<>();
        // Lấy tất cả các loại phòng (hoặc lọc theo roomTypeName nếu có)
        List<RoomType> roomTypes = roomTypeName != null && !roomTypeName.isEmpty()  ?
                roomTypeRepository.findByName(roomTypeName) :
                (List<RoomType>) roomTypeRepository.findAll();

        for (RoomType roomType : roomTypes) {
            boolean isAvailable = true;
            int availableRooms = Integer.MAX_VALUE;

            // Kiểm tra từng ngày trong khoảng thời gian này
            for (LocalDate date = checkInDate; !date.isAfter(checkOutDate.minusDays(1)); date = date.plusDays(1)) {
                int occupiedRooms = reservationRepository.countOccupiedRoomsByDateRoomTypeAndAdultCountAndChildCount(date, roomType.getId(), adultCount, childCount) ;
                int totalRooms = roomType.getQuantity();
                availableRooms = totalRooms - occupiedRooms;

                // Kiểm tra nếu phòng bị hết trong khoảng thời gian này
                if (availableRooms <= 0) {
                    isAvailable = false;

                    break;
                }
            }
            // Nếu loại phòng khả dụng trong tất cả các ngày, thêm vào kết quả
            if (isAvailable) {
                availableRoomInfoList.add(new AvailableRoomInfo(roomType, availableRooms));
            } else if (roomTypeName != null && !roomTypeName.isEmpty()) {
                // Nếu loại phòng cụ thể được chọn và hết phòng
                roomFullMessages.append("Room Type ").append(roomType.getName()).append(" is fully booked.");
            }
        }
        availableRoomInfoList.sort((info1, info2) -> {
            int maxAdult1 = info1.getRoomType().getMaxAdult();
            int maxAdult2 = info2.getRoomType().getMaxAdult();
            double price1 = info1.getRoomType().getPrice();
            double price2 = info2.getRoomType().getPrice();

            // Kiểm tra số lượng người lớn để quyết định sắp xếp theo maxAdult
            int maxAdultComparison;
            if (adultCount != null && adultCount < 4) {
                maxAdultComparison = Integer.compare(maxAdult1, maxAdult2); // Tăng dần
            } else {
                maxAdultComparison = Integer.compare(maxAdult2, maxAdult1); // Giảm dần
            }

            // Nếu maxAdult bằng nhau, sắp xếp theo giá phòng giảm dần
            if (maxAdultComparison == 0) {
                return Double.compare(price2, price1); // Giảm dần theo giá phòng
            }

            return maxAdultComparison; // Sắp xếp theo maxAdult trước
        });

        return availableRoomInfoList; // Trả về danh sách các phòng còn trống
    }



    /** Tìm phòng trống từ ngày... (trong vòng 14 ngày)*/
    public Map<String, Map<LocalDate, Integer>> findAvailableRoomsByDate(LocalDate checkInDate) {
        Map<String, Map<LocalDate, Integer>> availableRoomsByDate = new HashMap<>();

        // Lấy tất cả các loại phòng
        List<RoomType> roomTypes = (List<RoomType>) roomTypeRepository.findAll();

        for (RoomType roomType : roomTypes) {
            Map<LocalDate, Integer> availabilityByDay = new HashMap<>();

            // Kiểm tra từng ngày trong khoảng thời gian này
            for (LocalDate date = checkInDate; !date.isAfter(checkInDate.plusDays(14)); date = date.plusDays(1)) {
                int occupiedRooms = reservationRepository.countOccupiedRoomsByDateAndRoomType(date, roomType.getId());
                int totalRooms = roomType.getQuantity();

                int availableRooms = totalRooms - occupiedRooms;

                availabilityByDay.put(date, availableRooms);
            }

            availableRoomsByDate.put(roomType.getName(), availabilityByDay);
        }

        return availableRoomsByDate;
    }

}