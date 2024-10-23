package demo.HotelManagement.repository;

import demo.HotelManagement.entities.PaymentHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentHistoryRepository extends CrudRepository<PaymentHistory, Long> {
}