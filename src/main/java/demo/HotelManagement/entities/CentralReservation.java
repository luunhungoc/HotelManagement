package demo.HotelManagement.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="central_reservations")
public class CentralReservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="amount_paid")
    private int amountPaid;

    @Column(name="booking_date")
    @DateTimeFormat(pattern ="yyyy-MM-dd")
    private LocalDate bookingDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private CentralReservationStatus status;

    @OneToMany(mappedBy = "centralReservation",fetch = FetchType.EAGER)
    private List<Reservation> reservationList;
    public CentralReservation(){}

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    public CentralReservation(Long id, LocalDate bookingDate, List<Reservation> reservationList, Profile profile) {
        this.id = id;
        this.bookingDate = bookingDate;
        this.reservationList = reservationList;
        this.profile = profile;
    }

    public CentralReservationStatus getStatus() {
        return status;
    }

    public void setStatus(CentralReservationStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
    }
}