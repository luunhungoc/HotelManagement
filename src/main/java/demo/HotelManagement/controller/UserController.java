package demo.HotelManagement.controller;

import demo.HotelManagement.entities.*;
import demo.HotelManagement.repository.PaymentHistoryRepository;
import demo.HotelManagement.repository.ProfileRepository;
import demo.HotelManagement.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @GetMapping("/edit/{id}")
    public String editProfileForm(@PathVariable Long id, Model model) {
        Profile profile = profileService.findById(id);
        if (profile.getAvatar() == null || profile.getAvatar().isEmpty()) {
            profile.setAvatar("/img/profile/avatar-default.jpg");
        }
        model.addAttribute("profile", profile);
        model.addAttribute("genders", Gender.values());
        return "profile/profile-edit";
    }

    @PostMapping("/edit/{id}")
    public String editProfile(@PathVariable Long id, @ModelAttribute Profile profile) {

        profile.setId(id);
        if (profile.getGender() == Gender.Male) {
            profile.setTitle("Mr.");
        } else if (profile.getGender() == Gender.Female) {
            profile.setTitle("Ms.");
        } else {
        profile.setTitle("Mr./ Ms.");}
        // If no new avatar is uploaded, keep the current avatar
        if (profile.getAvatar() == null || profile.getAvatar().isEmpty()) {
            profile.setAvatar("/img/profile/avatar-default.jpg");
        } else
        {
            profile.setAvatar(profile.getAvatar());
        }

        profileService.save(profile);
        return "redirect:/user/edit/{id}";
    }

    @GetMapping("/payment-history/{id}")
    public String showPaymentForm(@PathVariable Long id, Model model) {
        Optional<PaymentHistory> paymentHistory = paymentHistoryRepository.findById(id);
        model.addAttribute("paymentHistory", paymentHistory);
        return "profile/payment-history";
    }

    @GetMapping("/delete/{id}")
    public String deleteProfile(@PathVariable long id) {
        Profile profile = profileService.findById(id);
        profileService.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/upload-profile-photo/{id}")
    public String uploadProfilePhoto(@RequestParam("image") MultipartFile file,
                                     @PathVariable Long id,
                                     RedirectAttributes redirectAttributes) throws IOException {

        try { if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/user/edit/" + id;
        }

        File saveFile = new ClassPathResource("static/img").getFile();
        File profileDir = new File(saveFile, "profile");
        if (!profileDir.exists()) {
            profileDir.mkdirs(); // Tạo thư mục nếu chưa tồn tại
        }

        Path path = Paths.get(profileDir.getAbsolutePath(), file.getOriginalFilename());

        // Lưu file vào thư mục
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // Cập nhật đường dẫn avatar trong database
        String profileUrl = "/img/profile/" + file.getOriginalFilename();
        profileService.updateImageUrl(id, profileUrl);

        redirectAttributes.addFlashAttribute("message", "Profile picture uploaded successfully.");

    }catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Failed to upload profile picture.");
        }
        return "redirect:/user/edit/" + id;
}



}