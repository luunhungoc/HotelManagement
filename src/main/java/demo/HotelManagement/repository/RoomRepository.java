package demo.HotelManagement.repository;

import demo.HotelManagement.entities.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends CrudRepository<Room,Long> {

    List<Room> findByRoomTypeCodeContainingOrNumber(String code, int number);
    List<Room> findAll(Sort number);

}
