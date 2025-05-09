package com.github.juanmanuel.nottodaytomorrow.services;

import com.github.juanmanuel.nottodaytomorrow.exceptions.NotFoundException;
import com.github.juanmanuel.nottodaytomorrow.models.Bill;
import com.github.juanmanuel.nottodaytomorrow.repositories.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BillService {
    @Autowired
    private BillRepository billRepository;

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
        return billRepository.save(bill);
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

    /*
    * HACER FUNCION PARA QUE AL INSERTAR UNA FACTURA,
    * SE CREEN CADA UNA PARA CADA USUARIO AUTOMATICAMENTE,
    * RECOGIENDO LOS USUARIOS DEL EQUIPO Y ASIGNANDO UN BILLUSER
    * PARA CADA UNO, DIVIENDO EL GASTO ENTRE EL NUMERO DE USUARIOS
    * */
}
