package demo.HotelManagement.repository;

import demo.HotelManagement.entities.Room;
import demo.HotelManagement.entities.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction,Long> {
    List<Transaction> findByReservationId(Long reservationId);
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.reservation.id = :reservationId AND t.transactionType.typeSubGroup = 'ROM'")
    Long getTotalRoomRevenueByReservationId(@Param("reservationId") Long reservationId);
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.reservation.id = :reservationId AND t.transactionType.typeGroup = 'REV' AND t.transactionType.typeSubGroup != 'ROM'")
    Long getTotalOtherRevenueByReservationId(@Param("reservationId") Long reservationId);

}
