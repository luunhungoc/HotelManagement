package demo.HotelManagement.service;

import demo.HotelManagement.entities.Room;
import demo.HotelManagement.entities.Transaction;
import demo.HotelManagement.repository.RoomRepository;
import demo.HotelManagement.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> findAll() {
        return (List<Transaction>) transactionRepository.findAll();
    }

    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    public void saveAll(List<Transaction> transaction) {
        transactionRepository.saveAll(transaction);
    }

    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public List<Transaction> getTransactionsByReservationId(Long reservationId) {
        return transactionRepository.findByReservationId(reservationId);
    }

    public Long getTotalRoomRevenueByReservationId(Long reservationId) {
        // Gọi phương thức trong TransactionRepository để tính tổng amount
        return transactionRepository.getTotalRoomRevenueByReservationId(reservationId);
    }
    public Long getTotalOtherRevenueByReservationId(Long reservationId) {
        // Gọi phương thức trong TransactionRepository để tính tổng amount
        return transactionRepository.getTotalOtherRevenueByReservationId(reservationId);
    }

}