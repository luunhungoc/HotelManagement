package demo.HotelManagement.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.HotelManagement.entities.*;
import demo.HotelManagement.repository.CentralReservationRepository;
import demo.HotelManagement.repository.PaymentHistoryRepository;
import demo.HotelManagement.repository.ReservationRepository;
import demo.HotelManagement.session.CartSession;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
public class PaymentController {
    @Autowired
    PaymentHistoryRepository paymentHistoryRepository;
    @Autowired
    CentralReservationRepository centralReservationRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @RequestMapping("/payment")
    public String viewPayment(Model model) {
        return "payment/paypal";
    }
    @RequestMapping("/payment-success")
    public String showSuccessPage(@RequestParam("response") String response, Model model) {
        // Giải mã và thêm thông tin từ response vào model nếu cần thiết
        model.addAttribute("response", response);
        return "payment/success"; // Trả về template `payment/success.html`
    }
    @RequestMapping("/payment-error")
    public String showErrorPage(Model model) {

        return "payment/error";
    }
    @PostMapping("/payment-success")
    public ResponseEntity<PaymentHistory> paymentSuccess(@RequestBody JsonNode fullResponse, HttpSession session) {
        try {
            // Bước 1: Giải mã JSON thành đối tượng PaymentHistory
            PaymentHistory paymentEntity = decodeJsonToPaymentEntity(fullResponse);

            if (paymentEntity != null) {
                // Bước 2: Lưu paymentEntity vào cơ sở dữ liệu
                paymentHistoryRepository.save(paymentEntity);

                // Bước 3: Lấy thông tin profile từ session
                Profile profile = (Profile) session.getAttribute("profile");
                List<CartSession> cartList = (List<CartSession>) session.getAttribute("cartList");

                // Bước 4: Tạo một CentralReservation mới
                CentralReservation centralReservation = new CentralReservation();
                centralReservation.setProfile(profile);  // Gán profile cho centralReservation
                centralReservation.setBookingDate(LocalDate.now());
                centralReservation.setStatus(CentralReservationStatus.valueOf("COMPLETED"));
                centralReservationRepository.save(centralReservation);

                // Bước 5: Tạo và lưu các Reservation
                for (CartSession item : cartList) {
                    Reservation reservation = new Reservation();
                    reservation.setRoomType(item.getRoomType());
                    reservation.setQuantity(item.getQuantity());
                    reservation.setMarket("ONL");
                    reservation.setRoomToCharge(item.getRoomType().getCode());
                    reservation.setCentralReservation(centralReservation);  // Liên kết với CentralReservation
                    reservation.setProfile(profile);
                    reservation.setCheckInDate(item.getCheckInDate());
                    reservation.setStatus(ReservationStatus.valueOf("GUARANTEED"));

                    reservationRepository.save(reservation);
                }

                // Bước 6: Gửi email cho khách hàng (logic gửi email ở đây)
                // sendEmailToCustomer(profile.getEmail(), paymentEntity);

                // Bước 7: Xóa giỏ hàng sau khi đặt hàng
                session.removeAttribute("cartList");

                // Trả về phản hồi thành công
                return ResponseEntity.ok(paymentEntity);
            } else {
                // Xử lý nếu có lỗi trong quá trình giải mã JSON
                return new ResponseEntity<>(new PaymentHistory(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            // Xử lý nếu có lỗi
            e.printStackTrace();
            return new ResponseEntity<>(new PaymentHistory(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    private PaymentHistory decodeJsonToPaymentEntity(JsonNode jsonNode) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //  JsonNode jsonNode = objectMapper.readTree(json);

            PaymentHistory paymentEntity = new PaymentHistory();
            paymentEntity.setOrderID(jsonNode.get("orderID").asText());
            paymentEntity.setPayerID(jsonNode.get("payerID").asText());
            paymentEntity.setPayerName(jsonNode.get("details").get("payer").get("name").get("given_name").asText());
            paymentEntity.setPayerAddress(jsonNode.get("details").get("payer").get("address").get("country_code").asText());
            paymentEntity.setPayerEmail(jsonNode.get("details").get("payer").get("email_address").asText());
            paymentEntity.setPaymentAmount(jsonNode.get("details").get("purchase_units").get(0).get("amount").get("value").asText());
            //paymentEntity.setPaymentCurrency(jsonNode.get("details").get("purchase_units").get(0).get("amount").get("currency_code").asText());
            paymentEntity.setPaymentStatus(jsonNode.get("details").get("status").asText());
            paymentEntity.setPaymentDate(LocalDate.now());
            paymentEntity.setPaymentTime(LocalTime.now());

            return paymentEntity;
        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý exception nếu có
            return null;
        }
    }
}