package demo.HotelManagement.repository;

import demo.HotelManagement.session.CartSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends CrudRepository<CartSession,Long> {
}
