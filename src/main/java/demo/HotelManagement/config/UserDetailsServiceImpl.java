package demo.HotelManagement.config;

import demo.HotelManagement.entities.Profile;
import demo.HotelManagement.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Profile user = (Profile) profileRepository.findByLoginName(username);

        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return new CustomUser(user);
    }

}
