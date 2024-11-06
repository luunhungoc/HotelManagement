package demo.HotelManagement.service;

import demo.HotelManagement.entities.TransactionType;
import demo.HotelManagement.repository.TransactionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TransactionTypeService {
    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    public List<TransactionType> findAll() {
        return (List<TransactionType>) transactionTypeRepository.findAll();
    }
    public TransactionType save(TransactionType transactionType) {
        return transactionTypeRepository.save(transactionType);
    }
    public void deleteById(Long id) {
        transactionTypeRepository.deleteById(id);
    }
    public void saveAll(List<TransactionType > transaction) {
        transactionTypeRepository.saveAll(transaction);
    }

    public TransactionType findById(Long id) {
        return transactionTypeRepository.findById(id).orElse(null);
    }
    public TransactionType findByCode(String code) {
        return transactionTypeRepository.findByCode(code);
    }
}
