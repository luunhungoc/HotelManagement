package demo.HotelManagement.entities;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name="reservations")
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomToCharge;
    private int quantity;
    private String partyId;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = true)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @ManyToOne
    @JoinColumn(name="central_reservation_id", nullable = true)
    private CentralReservation centralReservation;

    @Column(name="check_in_date")
    @DateTimeFormat(pattern ="yyyy-MM-dd")
    private LocalDate checkInDate;

    @Column(name="check_out_date")
    @DateTimeFormat(pattern ="yyyy-MM-dd")
    private LocalDate checkOutDate;

    private int nights;
    private int adult;
    private int child;

    private String specialRequest;
    private String market;

    @Column(length = 1000)
    private String comment;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public Reservation() {

    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public int getNights() {
        return nights;
    }

    public String getRoomToCharge() {
        return roomToCharge;
    }

    public void setRoomToCharge(String roomToCharge) {
        this.roomToCharge = roomToCharge;
    }

    public CentralReservation getCentralReservation() {
        return centralReservation;
    }

    public void setCentralReservation(CentralReservation centralReservation) {
        this.centralReservation = centralReservation;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }


    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }

    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }


}

