package demo.HotelManagement.service;

import demo.HotelManagement.entities.*;
import demo.HotelManagement.repository.ReservationRepository;
import demo.HotelManagement.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    public List<Room> findAll() {
        return (List<Room>) roomRepository.findAll(Sort.by("number"));
    }
    public Room save(Room room) {
        return roomRepository.save(room);
    }
    public void deleteById(Long id) {
        roomRepository.deleteById(id);
    }
    public void saveAll(List<Room> rooms) {
        roomRepository.saveAll(rooms);
    }

    public Room findById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    public List<Room> getRoomsByCodeOrNumber(String code, int number) {
        return roomRepository.findByRoomTypeCodeContainingOrNumber(code,number);
    }

    public List<Room> findRoomsByDate(LocalDate searchDate) {
        List<Room> rooms = (List<Room>) roomRepository.findAll();
        List<Room> availableRooms = new ArrayList<>();

        for (Room room : rooms) {
            // Lấy danh sách các đặt phòng cho phòng này
            List<Reservation> reservations = reservationRepository
                    .findReservationsByRoomNumberAndDate(room.getNumber(), searchDate);

            boolean isOccupied = false;

            for (Reservation reservation : reservations) {
                // Kiểm tra nếu có bất kỳ đặt phòng nào chưa check-out vào ngày searchDate
                if (reservation.getCheckOutDate().isAfter(searchDate)) {
                    isOccupied = true;
                    break;
                }
            }

            // Nếu có đặt phòng chưa check-out thì đặt là OCCUPIED, ngược lại là VACANT
            if (isOccupied) {
                room.setRoomStatus(RoomStatus.OCCUPIED);
                availableRooms.add(room);
            } else {
                room.setRoomStatus(RoomStatus.VACANT);
                availableRooms.add(room);
            }
            room.setReservationList(reservations);
        }
        return availableRooms;
    }

}