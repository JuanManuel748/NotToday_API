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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


    public List<Bill> getAllwDetails() throws NotFoundException {
        List<Bill> bills = billRepository.findAll();
        if (bills.isEmpty()) {
            throw new NotFoundException("No bills found", Bill.class);
        }
        for (Bill b : bills) {
            b.setBillsUsers(buService.getByBillId(b.getId()));
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

    public Bill getByIdwDetails(Long id) throws NotFoundException {
        Optional<Bill> bill = billRepository.findById(id);
        if (bill.isPresent()) {
            Bill b = bill.get();
            b.setBillsUsers(buService.getByBillId(b.getId()));
            return b;
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
        System.out.println("Updating bill with id: " + id);
        Optional<Bill> existing = billRepository.findById(id);
        if (existing.isPresent()) {
            buService.delete(new Bill(id));
            Bill updatedBill = existing.get();
            updatedBill.setDescription(bill.getDescription());
            updatedBill.setAmount(bill.getAmount());
            updatedBill.setCreatedAt(bill.getCreatedAt());
            updatedBill.setTeam(bill.getTeam());
            updatedBill.setPayer(bill.getPayer());
            buService.create(bill);
            return billRepository.save(updatedBill);
        } else {
            throw new NotFoundException("Bill not found with id: " + id, Bill.class);
        }
    }

    public boolean delete(Long id) throws NotFoundException {
        Optional<Bill> bill = billRepository.findById(id);
        if (bill.isPresent()) {
            Bill deletedBill = bill.get();
            buService.delete(deletedBill);
            billRepository.delete(deletedBill);
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
        List<Bill> initialBills = billRepository.findByTeam(teamId);
        if (initialBills.isEmpty()) {
            throw new NotFoundException("No bills found for the specified team", Bill.class);
        }
        List<Bill> billsToKeep = new ArrayList<>();
        for (Bill b : initialBills) {
            b.setBillsUsers(buService.getByBillId(b.getId()));
            if (b.getBillsUsers() != null) {
                // Eliminar las partes de la factura que están totalmente pagadas
                b.getBillsUsers().removeIf(bu ->
                        bu.getPaid() != null &&
                                bu.getOwed() != null &&
                                bu.getPaid().compareTo(bu.getOwed()) >= 0
                );
            }
            // Conservar la factura solo si todavía tiene partes de usuario asociadas (no pagadas o parcialmente pagadas)
            if (b.getBillsUsers() != null && !b.getBillsUsers().isEmpty()) {
                b.setBillsUsers(buService.getByBillId(b.getId()));
                billsToKeep.add(b);
            }
        }
        // Si después de filtrar no quedan facturas, se devuelve una lista vacía.
        return billsToKeep;
    }

    public List<Bill> findByTeamPaid(Long teamId) {
        List<Bill> initialBills = billRepository.findByTeam(teamId);
        if (initialBills.isEmpty()) {
            throw new NotFoundException("No bills found for the specified team", Bill.class);
        }

        List<Bill> fullyPaidBills = new ArrayList<>();
        for (Bill b : initialBills) {
            List<BillsUser> billsUsers = buService.getByBillId(b.getId());
            b.setBillsUsers(billsUsers);

            if (billsUsers == null || billsUsers.isEmpty()) {
                // Considerar si una factura sin BillsUser debe ser incluida.
                // Por ahora, la excluiremos ya que no se puede determinar si está "completamente pagada".
                // O podría considerarse pagada si no tiene deudas. Depende de la lógica de negocio.
                // Si se quisiera incluir facturas sin BillsUser como pagadas, se añadiría aquí.
                // fullyPaidBills.add(b);
                continue;
            }

            boolean allPartsPaid = true;
            for (BillsUser bu : billsUsers) {
                if (bu.getPaid() == null || bu.getOwed() == null || bu.getPaid().compareTo(bu.getOwed()) < 0) {
                    allPartsPaid = false;
                    break;
                }
            }

            if (allPartsPaid) {
                fullyPaidBills.add(b);
            }
        }
        return fullyPaidBills;
    }

    public List<Bill> findByTeamAll(Long teamId) {
        List<Bill> bills = billRepository.findByTeam(teamId);
        if (!bills.isEmpty()) {
            for (Bill b : bills) {
                b.setBillsUsers(buService.getByBillId(b.getId()));
            }

            return bills;
        } else {
            throw new NotFoundException("No bills found for the specified team", Bill.class);
        }
    }

    public List<Bill> findByPayer(Long payerId) {
        List<Bill> bills = billRepository.findByPayer(payerId);
        if (!bills.isEmpty()) {
            for (Bill b : bills) {
                b.setBillsUsers(buService.getByBillId(b.getId()));
            }
            return bills;
        } else {
            throw new NotFoundException("No bills found for the specified payer", Bill.class);
        }
    }

    public boolean payBill(Long billId, Long userId) throws NotFoundException {
        return buService.payBill(billId, userId);
    }

    public List<Bill> getPaidBillsByUserId(Long userId) {
        List<BillsUser> billsUsers = buService.getPaidBillsByUserId(userId);
        if (billsUsers.isEmpty()) {
            throw new NotFoundException("No paid bills found for user id: " + userId, Bill.class);
        }
        return billsUsers.stream().map(BillsUser::getBill).collect(Collectors.toList());
    }

    public List<Bill> getUnpaidBillsByUserId(Long userId) {
        List<BillsUser> billsUsers = buService.getUnpaidBillsByUserId(userId);
        if (billsUsers.isEmpty()) {
            throw new NotFoundException("No unpaid bills found for user id: " + userId, Bill.class);
        }
        return billsUsers.stream().map(BillsUser::getBill).collect(Collectors.toList());
    }

    public BigDecimal getTotalOwedAmountByUserId(Long userId) {
        List<BillsUser> billsUsers = buService.getUnpaidBillsByUserId(userId);
        if (billsUsers.isEmpty()) {
            throw new NotFoundException("No bills found for user id: " + userId, Bill.class);
        }
        return billsUsers.stream()
                .map(bu -> bu.getOwed() != null ? bu.getOwed() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

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
