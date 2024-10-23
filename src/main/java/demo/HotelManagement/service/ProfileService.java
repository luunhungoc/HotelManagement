package demo.HotelManagement.service;

import demo.HotelManagement.entities.Profile;
import demo.HotelManagement.entities.Profile;
import demo.HotelManagement.entities.Room;
import demo.HotelManagement.repository.ProfileRepository;
import demo.HotelManagement.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

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

}