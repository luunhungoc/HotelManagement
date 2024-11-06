package demo.HotelManagement.service;

import demo.HotelManagement.entities.Reservation;
import demo.HotelManagement.entities.Transaction;
import demo.HotelManagement.entities.TransactionType;
import demo.HotelManagement.repository.ReservationRepository;
import demo.HotelManagement.repository.TransactionRepository;
import demo.HotelManagement.repository.TransactionTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;

@Service
public class ScheduledTaskService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionTypeRepository transactionTypeRepository;
    /**
     * Lịch trình chạy lúc 00:01 mỗi ngày để thêm tiền phòng vào bảng Transaction.
     */
    @Scheduled(cron = "0 21 2 * * ?")
    @Transactional
    public void addRoomChargeEveryNight() {
        // Lấy tất cả các phòng đang ở trạng thái "CheckIn"
        List<Reservation> activeReservations = reservationRepository.findActiveReservations(LocalDate.now());
        // Lấy loại giao dịch có code = 1000
        TransactionType roomChargeType = transactionTypeRepository.findByCode("1000");
        for (Reservation reservation : activeReservations) {
            // Kiểm tra nếu đặt phòng vẫn còn hiệu lực (check-out sau thời điểm hiện tại)
            if (reservation.getCheckOutDate().isAfter(LocalDate.now())){
                // Tạo Transaction mới cho phí phòng mỗi đêm
                Transaction roomCharge = new Transaction();
                roomCharge.setTransactionType(roomChargeType);
                roomCharge.setQuantity(1);
                roomCharge.setAmount(reservation.getRoomType().getPrice());
                roomCharge.setDate(LocalDate.now());
                roomCharge.setReservation(reservation);

                // Lưu vào Transaction
                transactionRepository.save(roomCharge);
            }
        }
    }
}