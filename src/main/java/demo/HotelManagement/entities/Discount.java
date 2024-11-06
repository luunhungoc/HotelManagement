package demo.HotelManagement.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="discounts")
public class Discount implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String code;

    private double percentage;  // Percentage
    private LocalDate startDate;  // Discount start date
    private LocalDate endDate;    // Discount end date

    @Enumerated(EnumType.STRING)
    private DiscountStatus status;

    @OneToMany(mappedBy = "discount",fetch = FetchType.EAGER)
    private List<Reservation> reservationList;
    @ManyToMany
    private List<RoomType> roomTypes;  // RoomTypes that the discount applies to

    // Method to check if discount is valid for today
    public boolean isDiscountActive() {
        return LocalDate.now().isAfter(startDate) && LocalDate.now().isBefore(endDate);
    }
    public Discount() {

    }

    public DiscountStatus getStatus() {
        return status;
    }

    public void setStatus(DiscountStatus status) {
        this.status = status;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<RoomType> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(List<RoomType> roomTypes) {
        this.roomTypes = roomTypes;
    }
}

