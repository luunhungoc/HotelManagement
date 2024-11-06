package demo.HotelManagement.service;

import demo.HotelManagement.entities.Profile;
import demo.HotelManagement.repository.ProfileRepository;
import demo.HotelManagement.repository.ProfileRepository;
import demo.HotelManagement.util.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Profile> findAll() {
        return (List<Profile>) profileRepository.findAll();
    }
    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }
    public void deleteById(Long id) {
        profileRepository.deleteById(id);
    }
    public void saveAll(List<Profile> profiles) {
        profileRepository.saveAll(profiles);
    }

    public Profile findById(Long id) {
        return profileRepository.findById(id).orElse(null);
    }
    public void updateImageUrl(Long profileId, String imageUrl) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        profile.setAvatar(imageUrl);
        profileRepository.save(profile);
    }
    public Page<Profile> getProfiles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return profileRepository.findAll(pageable);
    }


    public Profile saveUser(Profile user) {
        user.setRole("ROLE_USER");
        user.setEnable(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);

        String encodePassword = passwordEncoder.encode(user.getLoginPassword());
        user.setLoginPassword(encodePassword);
        Profile saveUser = profileRepository.save(user);
        return saveUser;
    }

    public Profile getUserByEmail(String email) {
        return profileRepository.findByLoginName(email);
    }


    public List<Profile> getUsers(String role) {
        return profileRepository.findByRole(role);
    }


    public Boolean updateAccountStatus(Long id, Boolean status) {

        Optional<Profile> findByuser = profileRepository.findById(id);

        if (findByuser.isPresent()) {
            Profile userDtls = findByuser.get();
            userDtls.setEnable(status);
            profileRepository.save(userDtls);
            return true;
        }

        return false;
    }

    public void increaseFailedAttempt(Profile user) {
        int attempt = user.getFailedAttempt() + 1;
        user.setFailedAttempt(attempt);
        profileRepository.save(user);
    }

    public void userAccountLock(Profile user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        profileRepository.save(user);
    }

    public boolean unlockAccountTimeExpired(Profile user) {

        long lockTime = user.getLockTime().getTime();
        long unLockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;

        long currentTime = System.currentTimeMillis();

        if (unLockTime < currentTime) {
            user.setAccountNonLocked(true);
            user.setFailedAttempt(0);
            user.setLockTime(null);
            profileRepository.save(user);
            return true;
        }

        return false;
    }

    public void resetAttempt(int userId) {

    }

    public void updateUserResetToken(String email, String resetToken) {
        Profile findByEmail = profileRepository.findByLoginName(email);
        findByEmail.setResetToken(resetToken);
        profileRepository.save(findByEmail);
    }

    public Profile getUserByToken(String token) {
        return profileRepository.findByResetToken(token);
    }

    public Profile saveAdmin(Profile user) {
        user.setRole("ROLE_ADMIN");
        user.setEnable(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);

        String encodePassword = passwordEncoder.encode(user.getLoginPassword());
        user.setLoginPassword(encodePassword);
        Profile saveUser = profileRepository.save(user);
        return saveUser;
    }

    public Boolean existsEmail(String email) {
        return profileRepository.existsByLoginName(email);
    }

}