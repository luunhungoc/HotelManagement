package demo.HotelManagement.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name="transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private double amount;

    @OneToMany(mappedBy = "transaction",fetch = FetchType.EAGER)
    private List<PaymentHistory> paymentHistoryList;
    public Transaction(){}

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "transaction_type_id", nullable = false)  // Sửa đổi tên cột
    private TransactionType transactionType;


    private String note;

    // Constructors, Getters, Setters


    public Transaction(Long id, LocalDate date, double amount, Reservation reservation, TransactionType transactionType, String note) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.reservation = reservation;
        this.transactionType = transactionType;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
