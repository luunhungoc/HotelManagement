package demo.HotelManagement.repository;

import demo.HotelManagement.entities.Transaction;
import demo.HotelManagement.entities.TransactionType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionTypeRepository extends CrudRepository<TransactionType,Long> {

}
