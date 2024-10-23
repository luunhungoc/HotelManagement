package demo.HotelManagement.repository;

import demo.HotelManagement.entities.RoomType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface  RoomTypeRepository extends CrudRepository<RoomType,Long> {
    List<RoomType> findByName(String name);
    List<RoomType> findByCodeContainingOrNameContaining(String code, String name);
}
