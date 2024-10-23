package demo.HotelManagement.service;

import demo.HotelManagement.entities.CentralReservation;
import demo.HotelManagement.repository.CentralReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CentralReservationService {
    @Autowired
    private CentralReservationRepository centralReservationRepository;

    public CentralReservation findById(Long id) {
        return centralReservationRepository.findById(id).orElse(null);
    }
}
