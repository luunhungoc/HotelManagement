package demo.HotelManagement.service;

import demo.HotelManagement.entities.Reservation;
import demo.HotelManagement.entities.Room;
import demo.HotelManagement.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static org.thymeleaf.util.StringUtils.isEmpty;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    public List<Reservation> findAll() {
        return (List<Reservation>) reservationRepository.findAll();
    }
    public List<Reservation> findByProfileId(Long profileId) {
        return (List<Reservation>) reservationRepository.findByProfileId(profileId);
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

    public Page<Reservation> getReservations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reservationRepository.findAllByOrderByCheckInDateAsc(pageable);
    }

    public Page<Reservation> searchReservations(String lastName, String firstName, Long reservationId,
                                                LocalDate arrivalFrom, LocalDate arrivalTo, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

            if (!firstName.isEmpty() && !lastName.isEmpty()  && reservationId != null && arrivalFrom != null && arrivalTo != null) {
            return reservationRepository.findByProfileLastNameAndProfileFirstNameAndIdAndCheckInDateBetweenOrderByCheckInDateAsc(
                    lastName, firstName, reservationId, arrivalFrom, arrivalTo,pageable);
        }
        if (!firstName.isEmpty() && !lastName.isEmpty()  && reservationId != null && arrivalFrom == null && arrivalTo == null) {
            return reservationRepository.findByProfileLastNameAndProfileFirstNameAndIdOrderByCheckInDateAsc(
                    lastName, firstName, reservationId,pageable);
        }
        if (!firstName.isEmpty() && !lastName.isEmpty()  && reservationId == null && arrivalFrom != null && arrivalTo != null) {
            return reservationRepository.findByProfileLastNameAndProfileFirstNameAndCheckInDateBetweenOrderByCheckInDateAsc(
                    lastName, firstName, arrivalFrom, arrivalTo,pageable);
        }
        if (!firstName.isEmpty() && !lastName.isEmpty()  && reservationId == null && arrivalFrom == null && arrivalTo == null) {
            return reservationRepository.findByProfileLastNameAndProfileFirstNameOrderByCheckInDateAsc(lastName, firstName,pageable);
        }
        if (isEmpty(firstName) && isEmpty(lastName) && reservationId != null && arrivalFrom != null && arrivalTo != null) {
            return reservationRepository.findByIdAndCheckInDateBetweenOrderByCheckInDateAsc(reservationId, arrivalFrom, arrivalTo,pageable);
        }

        if (isEmpty(firstName) && isEmpty(lastName) && reservationId == null && arrivalFrom != null && arrivalTo != null) {
            return reservationRepository.findByCheckInDateBetweenOrderByCheckInDateAsc(arrivalFrom, arrivalTo,pageable);
        }
        if (isEmpty(lastName) && isEmpty(firstName)  && reservationId != null && arrivalFrom == null && arrivalTo == null) {
            return reservationRepository.findById(reservationId,pageable);
        }
        return reservationRepository.findAllByOrderByCheckInDateAsc(pageable); // Trả về tất cả nếu không có điều kiện tìm kiếm nào.
    }

    public  List<Reservation> findByRoom(Room room){
        return reservationRepository.findByRoom(room);
    }
}