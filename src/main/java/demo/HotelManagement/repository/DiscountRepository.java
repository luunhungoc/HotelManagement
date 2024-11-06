package demo.HotelManagement.repository;

import demo.HotelManagement.entities.Discount;
import demo.HotelManagement.entities.DiscountStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepository extends CrudRepository<Discount,Long> {
    List<Discount> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate startDate, LocalDate endDate);
    List<Discount> findByCode(String code);

}