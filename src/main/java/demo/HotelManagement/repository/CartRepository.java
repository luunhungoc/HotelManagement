package demo.HotelManagement.repository;

import demo.HotelManagement.entities.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends CrudRepository<Cart,Long> {
//    public Cart findByProductIdAndUserId(Integer productId, Integer userId);
//
//    public Integer countByUserId(Integer userId);
//
//    public List<Cart> findByUserId(Integer userId);
}
