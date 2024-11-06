package demo.HotelManagement.service;

import demo.HotelManagement.entities.RoomType;
import demo.HotelManagement.repository.CartRepository;
import demo.HotelManagement.repository.RoomTypeRepository;
import demo.HotelManagement.entities.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private CartRepository cartRepository;

    public void addToCart(Long roomTypeId,LocalDate checkInDate, LocalDate checkOutDate, Integer adults, Integer children) {
        // Fetch the product
        RoomType roomType = roomTypeRepository.findById(roomTypeId).orElseThrow(() -> new RuntimeException("Room Type not found"));

        // Create a new order item
        Cart cart = new Cart();
        cart.setRoomType(roomType);
        cart.setQuantity(1);
        cart.setPrice(roomType.getPrice());
        cart.setCheckInDate(checkInDate);
        cart.setCheckOutDate(checkOutDate);
        cart.setAdults(adults);
        cart.setChildren(children);
        cart.setNights((int) ChronoUnit.DAYS.between(checkInDate, checkOutDate));

        // Save the order item
        cartRepository.save(cart);
    }


//    public void updateQuantity(Long id) {
//
//        Optional<Cart> cart = cartRepository.findById(id);
//        int updateQuantity;
//
//        if (sy.equalsIgnoreCase("de")) {
//
//            if (updateQuantity <= 0) {
//                cartRepository.deleteById(id);
//            } else {
//                cart.setQuantity(updateQuantity);
//                cartRepository.save(cart);
//            }
//
//        } else {
//            updateQuantity = cart.getQuantity() + 1;
//            cart.setQuantity(updateQuantity);
//            cartRepository.save(cart);
//        }
//
//    }


//    public List<Cart> getCartsByUser(Integer userId) {
//        List<Cart> cartList = cartRepository.findByUserId(userId);
//        return cartList;
//    }

    public List<Cart> getCart() {
        // Fetch the current order
        List<Cart> cartList = (List<Cart>) cartRepository.findAll();
        // Return the order items
        return cartList;
    }

    public void clearCart() {
        // Delete all the order items
        cartRepository.deleteAll();
    }
    public void deleteCart(Long cartId) {
        cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Reservation not found"));
        cartRepository.deleteById(cartId);
    }
}