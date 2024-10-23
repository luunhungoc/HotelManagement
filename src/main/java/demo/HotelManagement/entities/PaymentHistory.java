package demo.HotelManagement.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "payment_history")
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderID;
    private String payerID;
    private String payerName;
    private String payerAddress;
    private String payerEmail;
    private String paymentAmount;
    private LocalDate paymentDate;
    private LocalTime paymentTime;
    private String paymentStatus;
    private String source;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = true)
    private Transaction transaction;

    // Constructors


    public PaymentHistory(Long id, String orderID, String payerID, String payerName, String payerAddress, String payerEmail, String paymentAmount, LocalDate paymentDate, LocalTime paymentTime, String paymentStatus, Transaction transaction) {
        this.id = id;
        this.orderID = orderID;
        this.payerID = payerID;
        this.payerName = payerName;
        this.payerAddress = payerAddress;
        this.payerEmail = payerEmail;
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
        this.paymentTime = paymentTime;
        this.paymentStatus = paymentStatus;
        this.transaction = transaction;
    }

    public PaymentHistory() {
        // Default constructor
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getPayerID() {
        return payerID;
    }

    public void setPayerID(String payerID) {
        this.payerID = payerID;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerAddress() {
        return payerAddress;
    }

    public void setPayerAddress(String payerAddress) {
        this.payerAddress = payerAddress;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}