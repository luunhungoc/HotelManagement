package demo.HotelManagement.repository;

import demo.HotelManagement.entities.Profile;
import demo.HotelManagement.entities.RoomType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends CrudRepository<Profile,Long> {
    List<Profile> findByLoginNameOrPhone(String loginName, String phone);
}
