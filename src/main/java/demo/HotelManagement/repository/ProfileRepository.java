package demo.HotelManagement.repository;

import demo.HotelManagement.entities.Profile;
import demo.HotelManagement.entities.Reservation;
import demo.HotelManagement.entities.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends CrudRepository<Profile,Long> {
    List<Profile> findByLoginNameOrPhone(String loginName, String phone);
    Profile findByLoginName(String loginName);
    public List<Profile> findByRole(String role);
    Page<Profile> findAll(Pageable pageable);
    public Profile findByResetToken(String token);

    public Boolean existsByLoginName(String email);
}
