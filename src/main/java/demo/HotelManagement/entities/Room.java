package demo.HotelManagement.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = true)
    private int number;

    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = true)
    private RoomType roomType;
    public Room(){}

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private RoomStatus roomStatus;

    @OneToMany(mappedBy = "room",fetch = FetchType.EAGER)
    private List<Reservation> reservationList;

    // Constructors, Getters, Setters

    public Room(Long id, int number, RoomType roomType, RoomStatus roomStatus) {
        this.id = id;
        this.number = number;
        this.roomType = roomType;
        this.roomStatus = roomStatus;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(RoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    public Room(long id, int number, RoomType roomType, List<Reservation> reservationList, RoomStatus roomStatus) {
        this.id = id;
        this.number = number;
        this.roomType = roomType;
        this.reservationList = reservationList;
        this.roomStatus = roomStatus;
    }
}
