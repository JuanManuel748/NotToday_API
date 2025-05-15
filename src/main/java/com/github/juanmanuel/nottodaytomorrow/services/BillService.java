package com.github.juanmanuel.nottodaytomorrow.services;

import com.github.juanmanuel.nottodaytomorrow.exceptions.NotFoundException;
import com.github.juanmanuel.nottodaytomorrow.exceptions.ValidationException;
import com.github.juanmanuel.nottodaytomorrow.models.Bill;
import com.github.juanmanuel.nottodaytomorrow.models.BillsUser;
import com.github.juanmanuel.nottodaytomorrow.models.User;
import com.github.juanmanuel.nottodaytomorrow.repositories.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BillService {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillsUserService buService;

    public List<Bill> getAll() throws NotFoundException {
        List<Bill> bills = billRepository.findAll();
        if (bills.isEmpty()) {
            throw new NotFoundException("No bills found", Bill.class);
        }
        return bills;
    }

    public Bill getById(Long id) throws NotFoundException {
        Optional<Bill> bill = billRepository.findById(id);
        if (bill.isPresent()) {
            return bill.get();
        } else {
            throw new NotFoundException("Bill not found with id: " + id, Bill.class);
        }
    }

    public Bill create(Bill bill) {
        if (validate(bill)){
            Bill saved = billRepository.save(bill);
            buService.create(bill);
            return saved;
        } else {
            return null;
        }
    }

    public Bill update(Long id, Bill bill) throws NotFoundException {
        Optional<Bill> existing = billRepository.findById(id);
        if (existing.isPresent()) {
            Bill updatedBill = existing.get();
            updatedBill.setDescription(bill.getDescription());
            updatedBill.setAmount(bill.getAmount());
            updatedBill.setCreatedAt(bill.getCreatedAt());
            updatedBill.setTeam(bill.getTeam());
            updatedBill.setPayer(bill.getPayer());
            return billRepository.save(updatedBill);
        } else {
            throw new NotFoundException("Bill not found with id: " + id, Bill.class);
        }
    }

    public boolean delete(Long id) throws NotFoundException {
        Optional<Bill> bill = billRepository.findById(id);
        if (bill.isPresent()) {
            billRepository.delete(bill.get());
            return true;
        } else {
            throw new NotFoundException("Bill not found with id: " + id, Bill.class);
        }
    }


    public List<Bill> findByAmountRange(BigDecimal min, BigDecimal max) {
        List<Bill> bills = billRepository.findByAmountRange(min, max);
        if (!bills.isEmpty()) {
            return bills;
        } else {
            throw new NotFoundException("No bills found in the specified amount range", Bill.class);
        }
    }

    public List<Bill> findByDescription(String description) {
        List<Bill> bills = billRepository.findByDescription(description);
        if (!bills.isEmpty()) {
            return bills;
        } else {
            throw new NotFoundException("No bills found with the specified description", Bill.class);
        }
    }

    public List<Bill> findByTeam(Long teamId) {
        List<Bill> bills = billRepository.findByTeam(teamId);
        if (!bills.isEmpty()) {
            return bills;
        } else {
            throw new NotFoundException("No bills found for the specified team", Bill.class);
        }
    }

    public List<Bill> findByPayer(Long payerId) {
        List<Bill> bills = billRepository.findByPayer(payerId);
        if (!bills.isEmpty()) {
            return bills;
        } else {
            throw new NotFoundException("No bills found for the specified payer", Bill.class);
        }
    }

    public boolean payBill(Long billId, Long userId) throws NotFoundException {
        return buService.payBill(billId, userId);
    }


    /* HACER FUNCION PARA QUE AL INSERTAR UNA FACTURA,
    * SE CREEN CADA UNA PARA CADA USUARIO AUTOMATICAMENTE,
    * RECOGIENDO LOS USUARIOS DEL EQUIPO Y ASIGNANDO UN BILLUSER
    * PARA CADA UNO, DIVIENDO EL GASTO ENTRE EL NUMERO DE USUARIOS */


    private boolean validate(Bill b) {
        boolean result = false;
        if (b.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            if (b.getDescription() != null && !b.getDescription().isEmpty()) {
                //if (b.getCreatedAt() != null) {
                    if (b.getTeam().getId() != null) {
                        if (b.getPayer().getId() != null) {
                            result = true;
                        } else {
                            throw new ValidationException("Payer ID is null", b.getPayer().getName(), b);
                        }
                    } else {
                        throw new ValidationException("Team ID is null", b.getTeam().getName(), b);
                    }
                /*} else {
                    throw new ValidationException("CreatedAt is null", "Bill CreatedAt Date", b);
                }*/
            } else {
                throw new ValidationException("Description is null or empty", b.getDescription(), b);
            }
        } else {
            throw new ValidationException("Amount is null or less than 0", b.getAmount().toString(), b);
        }


        return result;
    }
}
