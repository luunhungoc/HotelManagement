package demo.HotelManagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "room_types")
public class RoomType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private int maxAdult;
    private int maxChild;
    private int maxOccupancy;
    private double price;
    private double discount;
    private int quantity;

    
    @Column(name="room_type_photo",nullable = true, length = 64)
    private String roomPhoto;

    @OneToMany(mappedBy = "roomType",fetch = FetchType.EAGER)
    private List<Cart> carts;

    @JsonIgnore
    @OneToMany(mappedBy = "roomType",fetch = FetchType.EAGER)
    private List<Room> roomList;
    public RoomType(){}

    @OneToMany(mappedBy = "roomType",fetch = FetchType.EAGER)
    private List<Reservation> reservationList;

    @ManyToMany(mappedBy = "roomTypes")
    private List<Discount> discounts;  //
    // Constructors, Getters, Setters


    public RoomType(Long id, String code, String name, int maxAdult, int maxChild, int maxOccupancy, double price, double discount, int quantity, String roomPhoto, List<Cart> carts, List<Room> roomList, List<Reservation> reservationList) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.maxAdult = maxAdult;
        this.maxChild = maxChild;
        this.maxOccupancy = maxOccupancy;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
        this.roomPhoto = roomPhoto;
        this.carts = carts;
        this.roomList = roomList;
        this.reservationList = reservationList;
    }


    public RoomType(String code, String name, int maxAdult, int maxChild, int maxOccupancy, double price, double discount, int quantity) {
    }

    public double calculateDiscountedPrice(List<Discount> discounts) {
        double discountedPrice = this.price;
        for (Discount discount : discounts) {
            discountedPrice -= discountedPrice * (discount.getPercentage() / 100);
        }
        return discountedPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxAdult() {
        return maxAdult;
    }

    public void setMaxAdult(int maxAdult) {
        this.maxAdult = maxAdult;
    }

    public int getMaxChild() {
        return maxChild;
    }

    public void setMaxChild(int maxChild) {
        this.maxChild = maxChild;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRoomPhoto() {
        return roomPhoto;
    }

    public void setRoomPhoto(String roomPhoto) {
        this.roomPhoto = roomPhoto;
    }

    public List<Cart> getCartSessions() {
        return carts;
    }

    public void setCartSessions(List<Cart> carts) {
        this.carts = carts;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }
}

