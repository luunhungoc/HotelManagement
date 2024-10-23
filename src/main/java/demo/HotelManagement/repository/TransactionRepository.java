package demo.HotelManagement.repository;

import demo.HotelManagement.entities.Room;
import demo.HotelManagement.entities.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction,Long> {
    List<Transaction> findByReservationId(Long reservationId);
}
