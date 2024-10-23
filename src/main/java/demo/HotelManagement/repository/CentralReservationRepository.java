package demo.HotelManagement.repository;

import demo.HotelManagement.entities.CentralReservation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentralReservationRepository extends CrudRepository<CentralReservation,Long> {
}
