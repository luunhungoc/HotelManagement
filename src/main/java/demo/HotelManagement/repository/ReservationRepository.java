package demo.HotelManagement.repository;

import demo.HotelManagement.entities.CentralReservation;
import demo.HotelManagement.entities.Reservation;
import demo.HotelManagement.entities.Room;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation,Long> {
    @Query(value = "SELECT COUNT(b.id) FROM reservations b WHERE b.check_in_date <= :date AND b.check_out_date > :date AND b.room_type_id = :roomTypeId", nativeQuery = true)
    int countOccupiedRoomsByDateAndRoomType(@Param("date") LocalDate date, @Param("roomTypeId") Long roomTypeId);

    @Query(value = "SELECT COALESCE(SUM(r.quantity), 0) FROM reservations r " +
            "join room_types rt on r.room_type_id = rt.id " +
            "WHERE r.check_in_date <= :date AND r.check_out_date > :date " +
            "AND r.room_type_id = :roomTypeId AND rt.maxAdult >= :adultCount AND rt.maxChild >= :childCount AND (r.status = 'GUARANTEED' OR r.status = 'CHECKED_IN')", nativeQuery = true)
    int countOccupiedRoomsByDateRoomTypeAndAdultCountAndChildCount(@Param("date") LocalDate date, @Param("roomTypeId") Long roomTypeId, @Param("adultCount") int adultCount, @Param("childCount") int childCount);

    List<Reservation> findByCentralReservation(CentralReservation centralReservation);


}