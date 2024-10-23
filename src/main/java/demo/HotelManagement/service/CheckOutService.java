package demo.HotelManagement.service;


import demo.HotelManagement.config.VNPayConfig;
import demo.HotelManagement.entities.*;
import demo.HotelManagement.repository.CentralReservationRepository;
import demo.HotelManagement.repository.ProfileRepository;
import demo.HotelManagement.repository.ReservationRepository;
import demo.HotelManagement.service.CartService;
import demo.HotelManagement.session.CartSession;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
public class CheckOutService {
    @Autowired
    private CartService cartService;
    @Autowired
    private CentralReservationRepository centralReservationRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ProfileRepository profileRepository;

    @Transactional
    public CentralReservation checkOut(Profile profile) {

        // Fetch the cart items
        List<CartSession> cartItems = cartService.getCart();

        // Calculate the total price
        int total = 0;
        for (CartSession item : cartItems) {
            total += item.getQuantity() * item.getPrice();
        }

        // Create a new order
        CentralReservation centralReservation = new CentralReservation();
        centralReservation.setProfile(profile);
        centralReservation.setAmountPaid(total);
        centralReservation.setBookingDate(LocalDate.now());

        // Save the profile (if needed)
        profileRepository.save(profile);

        // Save the order
        centralReservation = centralReservationRepository.save(centralReservation);

        // Assign the order to the cart items and save them
        for (CartSession item : cartItems) {
            Reservation reservation = new Reservation();
            reservation.setRoomType(item.getRoomType());
            reservation.setQuantity(item.getQuantity());
            reservation.setMarket("ONL");
            reservation.setRoomToCharge(item.getRoomType().getCode());
            reservation.setCentralReservation(centralReservation);  // Liên kết với CentralReservation
            reservation.setProfile(profile);
            reservation.setCheckInDate(item.getCheckInDate());
            reservationRepository.save(reservation);
        }

        // Clear the cart
        cartService.clearCart();

        return centralReservation;
    }

    @Transactional
    public String checkOutWithPayOnline(Profile profile, String urlReturn) {
        // Fetch the cart items
        List<CartSession> cartItems = cartService.getCart();

        // Calculate the total price
        int total = 0;
        for (CartSession item : cartItems) {
            total += (int) (item.getQuantity() * item.getPrice() *item.getNights());
        }
        profileRepository.save(profile);
        // Create a new Central Reservation
        CentralReservation centralReservation = new CentralReservation();
        centralReservation.setProfile(profile);
        centralReservation.setAmountPaid(total);
        centralReservation.setBookingDate(LocalDate.now());
        centralReservation.setStatus(CentralReservationStatus.valueOf("PENDING"));

        // Save the order
        centralReservation = centralReservationRepository.save(centralReservation);

        // Assign the order to the cart items and save them
        for (CartSession item : cartItems) {
            Reservation reservation = new Reservation();
            reservation.setRoomType(item.getRoomType());
            reservation.setQuantity(item.getQuantity());
            reservation.setMarket("ONL");
            reservation.setRoomToCharge(item.getRoomType().getCode());
            reservation.setCentralReservation(centralReservation);  // Liên kết với CentralReservation
            reservation.setProfile(profile);
            reservation.setCheckInDate(item.getCheckInDate());
            reservation.setCheckOutDate(item.getCheckOutDate());
            reservation.setAdult(item.getAdults());
            reservation.setChild(item.getChildren());
            reservation.setStatus(ReservationStatus.valueOf("NON_GUARANTEED"));
            reservationRepository.save(reservation);
        }
        String paymentUrl = createOrder(centralReservation, urlReturn);

        return paymentUrl;
    }
    @Transactional
    public static String createOrder(CentralReservation centralReservation, String urlReturn) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
//      Id Transaction
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        String orderType = "order-type";


//        Put data in VNP
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
//       Type PayVN
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
//       Value Amount
        vnp_Params.put("vnp_Amount", String.valueOf(centralReservation.getAmountPaid()*100 ));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        // customer information
        vnp_Params.put("vnp_OrderInfo", String.valueOf(centralReservation.getId()));
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        vnp_Params.put("vnp_Locale", locate);
//---------------------------------------
        urlReturn += VNPayConfig.vnp_Returnurl;
        vnp_Params.put("vnp_ReturnUrl", urlReturn);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//----------------------------------------


        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        return paymentUrl;
    }

    @Transactional
    public int orderReturn(HttpServletRequest request) {
        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = null;
            String fieldValue = null;
            try {
                fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VNPayConfig.hashAllFields(fields);
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

}