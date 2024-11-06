package demo.HotelManagement.config;

import demo.HotelManagement.entities.Profile;
import demo.HotelManagement.repository.ProfileRepository;
import demo.HotelManagement.service.ProfileService;
import demo.HotelManagement.util.AppConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileService profileService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String email = request.getParameter("username");

        Profile userDtls = (Profile) profileRepository.findByLoginName(email);

        if (userDtls != null) {

            if (userDtls.getEnable()) {

                if (userDtls.getAccountNonLocked()) {

                    if (userDtls.getFailedAttempt() < AppConstant.ATTEMPT_TIME) {
                        profileService.increaseFailedAttempt(userDtls);
                    } else {
                        profileService.userAccountLock(userDtls);
                        exception = new LockedException("Your account is locked !! failed attempt 3");
                    }
                } else {

                    if (profileService.unlockAccountTimeExpired(userDtls)) {
                        exception = new LockedException("Your account is unlocked !! Please try to login");
                    } else {
                        exception = new LockedException("your account is Locked !! Please try after sometimes");
                    }
                }

            } else {
                exception = new LockedException("your account is inactive");
            }
        } else {
            exception = new LockedException("Email & password invalid");
        }

        super.setDefaultFailureUrl("/signin?error");
        super.onAuthenticationFailure(request, response, exception);
    }

}