package demo.HotelManagement.service;

import demo.HotelManagement.entities.Discount;
import demo.HotelManagement.entities.DiscountStatus;
import demo.HotelManagement.entities.Room;
import demo.HotelManagement.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {
    @Autowired
    private DiscountRepository discountRepository;

    public Discount save(Discount discount) {
        return discountRepository.save(discount);
    }

    public List<Discount> getAllDiscounts() {
        return (List<Discount>) discountRepository.findAll();
    }

    public Discount saveDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    public List<Discount> getApplicableDiscounts() {
        LocalDate today = LocalDate.now();
        return discountRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(today,today);
    }

    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }
    public Discount findById(Long id) {
        return discountRepository.findById(id).orElse(null);
    }

    public List<Discount> findByCode(String searchInput) {
        return discountRepository.findByCode(searchInput);
    }

    public void updateDiscountStatus() {
        List<Discount> discounts = (List<Discount>) discountRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Discount discount : discounts) {
            if (discount.getEndDate().isBefore(today)) {
                discount.setStatus(DiscountStatus.valueOf("INACTIVE"));
            } else {
                discount.setStatus(DiscountStatus.valueOf("ACTIVE"));
            }
        }
        discountRepository.saveAll(discounts); // Lưu các cập nhật vào DB
    }

}
