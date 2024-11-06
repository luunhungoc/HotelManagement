package demo.HotelManagement.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name="reservations")
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomTypeCurrent;
    private int quantity;
    private String partyId;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = true)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "discount_id", nullable = true)
    private Discount discount;

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
    @Column(name = "check_in_time", nullable = true)
    private LocalDateTime checkInTime;
    @Column(name = "check_out_time", nullable = true)
    private LocalDateTime checkOutTime;
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
}

