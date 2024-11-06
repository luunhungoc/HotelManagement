package demo.HotelManagement.controller;

import demo.HotelManagement.entities.*;
import demo.HotelManagement.repository.*;
import demo.HotelManagement.service.CartService;
import demo.HotelManagement.service.CheckOutService;
import demo.HotelManagement.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller

public class CheckOutController {

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    CentralReservationRepository centralReservationRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CheckOutService checkOutService;

    @Autowired
    EmailService emailService;

    @PostMapping(value = "/checkout")
    public String checkOut(@ModelAttribute Profile profile,
                           HttpServletRequest request, Model model) {
        StringBuilder profileMessages = new StringBuilder();

        // Fetch profiles by loginName or phone
        List<Profile> profiles = profileRepository.findByLoginNameOrPhone(profile.getLoginName(), profile.getPhone());

        if (!profiles.isEmpty()) {
            // If at least one profile is found, send the alert to sign in
            profileMessages.append("Email or phone number is already in use. Please sign in.");
            model.addAttribute("profileMessages", profileMessages.toString());
            return "website/complete-booking";  // Redirect to the complete-booking page
        }

        // Nếu email chưa tồn tại, lưu profile vào cơ sở dữ liệu và tiếp tục thanh toán
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = checkOutService.checkOutWithPayOnline(profile, baseUrl);

        System.out.println("vnpayUrl: " + vnpayUrl);
        return "redirect:" + vnpayUrl;

    }

    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model) throws IOException {
        int paymentStatus = checkOutService.orderReturn(request);
        String centralReservationId = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        double actualAmount = 0;
        if (totalPrice != null && !totalPrice.isEmpty()) {
            actualAmount = Long.parseLong(totalPrice) / 100.0;
        }

        CentralReservation centralReservation = centralReservationRepository.findById(Long.valueOf(centralReservationId))
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        // Nếu thanh toán thành công, cập nhật trạng thái "COMPLETED"
        if (paymentStatus == 1) {
            // Cập nhật trạng thái cho CentralReservation
            centralReservation.setStatus(CentralReservationStatus.COMPLETED);

            // Cập nhật trạng thái cho các Reservation liên quan
            List<Reservation> reservations = reservationRepository.findByCentralReservation(centralReservation);
            for (Reservation reservation : reservations) {
                reservation.setNights((int) ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate()));
                reservation.setStatus(ReservationStatus.GUARANTEED);
                reservationRepository.save(reservation); // Lưu thay đổi trạng thái cho Reservation
            }

            // Lưu CentralReservation với trạng thái mới
            centralReservationRepository.save(centralReservation);
            model.addAttribute("reservations", reservations);

            try {
                // Chuyển chuỗi này thành LocalDateTime với định dạng chính xác
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                LocalDateTime dateTime = LocalDateTime.parse(paymentTime, inputFormatter);

                // Extract the LocalDate (Payment Date) and LocalTime (Payment Time)
                LocalDate paymentDate = dateTime.toLocalDate();   // Extract Date
                LocalTime paymentTimeParsed = dateTime.toLocalTime(); // Extract Time

                // Định dạng lại thành "dd MMM yyyy HH:mm:ss"
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");
                String formattedDateTime = dateTime.format(outputFormatter);

                // Lưu thông tin vào cơ sở dữ liệu
                PaymentHistory payment = new PaymentHistory();
                payment.setPaymentAmount(totalPrice);
                payment.setPaymentDate(paymentDate);
                payment.setPaymentTime(paymentTimeParsed);
                payment.setOrderID(transactionId);
                payment.setPaymentStatus("COMPLETED");
                payment.setSource("VNPAY");
                // Lưu vào database qua PaymentRepository
                paymentHistoryRepository.save(payment);

                model.addAttribute("paymentTime", formattedDateTime);

            } catch (DateTimeParseException e) {
                // Xử lý lỗi định dạng nếu chuỗi không hợp lệ
                model.addAttribute("paymentTime", "Invalid date format");
            }

            model.addAttribute("centralReservationId", centralReservationId);
            model.addAttribute("totalPrice", actualAmount);
            model.addAttribute("transactionId", transactionId);

            // Clear the cart
            cartService.clearCart();

            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("loginName", centralReservation.getProfile().getLoginName());
            templateModel.put("reservations", reservations);
            templateModel.put("centralReservation", centralReservation);
            templateModel.put("totalPrice", actualAmount);
            emailService.sendHtmlEmail(centralReservation.getProfile().getLoginName(), "Confirmation of your reservation: La Siesta Danang Resort", "payment/VNPAY_successfulEmail.html", templateModel);
            return "payment/VNPAY_successful";
        }
        // Nếu thanh toán thất bại, chuyển đến trang thất bại
        return "payment/VNPAY_fail";
    }


}

