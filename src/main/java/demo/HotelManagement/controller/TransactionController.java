package demo.HotelManagement.controller;

import demo.HotelManagement.entities.*;
import demo.HotelManagement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class TransactionController {

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private TransactionTypeService transactionTypeService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transactions")
    @ResponseBody
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction,
                                                      @RequestParam Long reservationId) {
        Reservation reservation = reservationService.findById(reservationId);
        TransactionType transactionType = transactionTypeService.findByCode(transaction.getTransactionType().getCode());
        transaction.setReservation(reservation);
        transaction.setDate(LocalDate.now());
        // Xác định dấu của transactionAmount dựa trên typeGroup
        double amount = transaction.getAmount();
        if ("PAY".equalsIgnoreCase(transactionType.getTypeGroup())) {
            // Nếu là PAY, đổi dấu của transactionAmount
            amount = amount > 0 ? -amount : amount;
        }
        // Nếu là REV, giữ nguyên dấu
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        transactionService.save(transaction);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/transaction/type/{code}")
    @ResponseBody
    public ResponseEntity<TransactionType> getTransactionTypeByCode(@PathVariable String code) {
        TransactionType transactionType = transactionTypeService.findByCode(code);

        // Kiểm tra xem transactionType có null không
        if (transactionType == null) {
            return ResponseEntity.notFound().build(); // Trả về 404 nếu không tìm thấy
        }

        return ResponseEntity.ok(transactionType); // Trả về transactionType
    }

    @DeleteMapping("/transactions/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/transaction")
    public String showTransaction(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "7") int size,
                                  Model model)  {
        Page<Reservation> reservationPage = reservationService.getReservations(page, size);

        model.addAttribute("reservations", reservationPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reservationPage.getTotalPages());

        return "admin/reservation/reservation-list"; // Thymeleaf template
    }

    @GetMapping("/admin/transaction/edit/{id}")
    public String showTransactionDetail(@PathVariable Long id, Model model) {
        Transaction transaction = transactionService.findById(id);
        Reservation reservation=reservationService.findById(transaction.getReservation().getId());
        transactionService.save(transaction);
        model.addAttribute("transaction", transaction);
        model.addAttribute("reservation", reservation);
        return "admin/transaction/transaction-edit";
    }

    @PostMapping("/admin/transaction/edit/{id}")
    public String editReservationDetail(@PathVariable Long id, @ModelAttribute Reservation res, Model model) {

        Transaction transaction = transactionService.findById(id);

        transactionService.save(transaction); // Lưu Reservation đã cập nhật

        return "redirect:/admin/transaction/edit/{id}"; // Điều hướng sau khi lưu thành công
    }

}

