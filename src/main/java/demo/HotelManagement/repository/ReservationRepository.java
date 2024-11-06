package demo.HotelManagement.repository;

import demo.HotelManagement.entities.CentralReservation;
import demo.HotelManagement.entities.Reservation;
import demo.HotelManagement.entities.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<Reservation> findAllByOrderByCheckInDateAsc(Pageable pageable);
    Page<Reservation> findById(Long id,Pageable pageable);
    Page<Reservation> findByProfileLastNameAndProfileFirstNameAndIdAndCheckInDateBetweenOrderByCheckInDateAsc(
            String lastName, String firstName, Long reservationId, LocalDate arrivalFrom, LocalDate arrivalTo,Pageable pageable);

    Page<Reservation> findByProfileLastNameAndProfileFirstNameAndIdOrderByCheckInDateAsc(
            String lastName, String firstName, Long reservationId,Pageable pageable);

    Page<Reservation> findByProfileLastNameAndProfileFirstNameAndCheckInDateBetweenOrderByCheckInDateAsc(
            String lastName, String firstName, LocalDate arrivalFrom, LocalDate arrivalTo,Pageable pageable);

    Page<Reservation> findByProfileLastNameAndProfileFirstNameOrderByCheckInDateAsc(
            String lastName, String firstName,Pageable pageable);

    Page<Reservation> findByIdAndCheckInDateBetweenOrderByCheckInDateAsc(
            Long reservationId, LocalDate arrivalFrom, LocalDate arrivalTo,Pageable pageable);


    Page<Reservation> findByCheckInDateBetweenOrderByCheckInDateAsc(LocalDate arrivalFrom, LocalDate arrivalTo, Pageable pageable);

    @Query("SELECT r FROM Reservation r WHERE r.room.number = :roomNumber AND :searchDate BETWEEN r.checkInDate AND r.checkOutDate AND r.status IN ('CHECKED_IN', 'GUARANTEED')")
    List<Reservation> findReservationsByRoomNumberAndDate(@Param("roomNumber") int roomNumber, @Param("searchDate") LocalDate searchDate);

    List<Reservation> findByProfileId(Long profileId);

    List<Reservation> findByRoom(Room room);

    @Query("SELECT r FROM Reservation r WHERE r.status = 'CHECKED_IN' AND r.checkOutDate > :currentDate")
    List<Reservation> findActiveReservations(@Param("currentDate") LocalDate currentDate);
}