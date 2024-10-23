package demo.HotelManagement.service;

import demo.HotelManagement.entities.Room;
import demo.HotelManagement.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<Room> findAll() {
        return (List<Room>) roomRepository.findAll();
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
}