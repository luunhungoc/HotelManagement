package demo.HotelManagement.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name="transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private double amount;
    private int quantity;

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

}
