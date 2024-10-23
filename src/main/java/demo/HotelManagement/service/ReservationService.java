package demo.HotelManagement.service;

import demo.HotelManagement.entities.Reservation;
import demo.HotelManagement.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    public List<Reservation> findAll() {
        return (List<Reservation>) reservationRepository.findAll();
    }
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
    public void saveAll(List<Reservation> reservations) {
        reservationRepository.saveAll(reservations);
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

//    public List<Reservation> getreservationsByCodeOrNumber(String code, int number) {
//        return reservationRepository.findByReservationTypeCodeContainingOrNumber(code,number);
//    }
}