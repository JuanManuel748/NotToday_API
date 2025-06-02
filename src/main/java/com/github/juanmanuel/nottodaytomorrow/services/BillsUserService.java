package com.github.juanmanuel.nottodaytomorrow.services;

import com.github.juanmanuel.nottodaytomorrow.exceptions.NotFoundException;
import com.github.juanmanuel.nottodaytomorrow.models.Bill;
import com.github.juanmanuel.nottodaytomorrow.models.BillsUser;
import com.github.juanmanuel.nottodaytomorrow.models.BillsUserId;
import com.github.juanmanuel.nottodaytomorrow.models.User;
import com.github.juanmanuel.nottodaytomorrow.repositories.BillsUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillsUserService {
    @Autowired
    private BillsUserRepository buRepository;
    @Autowired
    private UserService userService;

    public List<BillsUser> create(Bill b) throws NotFoundException {
        List<BillsUser> resultList = new ArrayList<>();

        // Si tiene una lista de BillsUsers, se crean
        if (!b.getBillsUsers().isEmpty()) {
            for (BillsUser bu : b.getBillsUsers()) {
                if (bu.getBill() == null) {
                    bu.setBill(b);
                }
                if (bu.getUser() == null) {
                    throw new NotFoundException("User not found for BillsUser with id: " + bu.getId(), User.class);
                }
                if (bu.getId() == null) {
                    bu.setId(new BillsUserId(b.getId(), bu.getUser().getId()));
                }
                if (bu.getPaymentDate() == null) {
                    bu.setPaymentDate(LocalDate.now());
                }
                if (bu.getPaid().compareTo(bu.getOwed()) < 0) {
                    bu.setPaymentDate(null);
                }
            }
            System.out.println("Actualizando lista de BillsUsers");
            System.out.println("Lista de BillsUsers: " + b.getBillsUsers());
            buRepository.saveAll(b.getBillsUsers());
        } else {
            // Crear BillsUser para cada usuario de la factura
            List<User> users = userService.getUsersByTeamId(b.getTeam().getId());
            if (users.isEmpty()) {
                throw new NotFoundException("No users found for team id: " + b.getTeam().getId(), User.class);
            }
            BigDecimal amountperUser = b.getAmount().divide(new BigDecimal(users.size()), 2, RoundingMode.HALF_UP);
            for (User u: users) {
                BillsUser bu = new BillsUser(b, u, amountperUser, BigDecimal.ZERO); // Payer initially owes too
                if (b.getPayer() != null && u.getId().equals(b.getPayer().getId())) {
                    bu.setPaid(amountperUser);
                    bu.setPaymentDate(LocalDate.now());
                }
                buRepository.save(bu);
                resultList.add(bu);
            }
        }
        return resultList;
    }

    public boolean delete(Bill b) throws NotFoundException {
        List<BillsUser> billsUsers = buRepository.findByBill(b.getId());
        // No lanzar NotFoundException si está vacío, simplemente no hay nada que borrar.
        if (!billsUsers.isEmpty()) {
            buRepository.deleteAll(billsUsers);
        }
        return true;
    }

    public List<BillsUser> getAll() throws NotFoundException {
        List<BillsUser> billsUsers = buRepository.findAll();
        if (billsUsers.isEmpty()) {
            throw new NotFoundException("No bills found", BillsUser.class);
        }
        for (BillsUser bu : billsUsers) {
            bu = parseToDTO(bu);
        }
        return billsUsers;
    }

    public BillsUser getByID(Long billId, Long userId) throws NotFoundException {
        Optional<BillsUser> bu = buRepository.findByPK(billId, userId);
        if (bu.isPresent()) {
            BillsUser billsUser = bu.get();
            billsUser = parseToDTO(billsUser);
            return billsUser;
        }
        throw new NotFoundException("BillsUser not found with billId: " + billId + " and userId: " + userId, BillsUser.class);
    }

    public List<BillsUser> getByBillId(Long billId) throws NotFoundException {
        List<BillsUser> billsUsers = buRepository.findByBill(billId);
        if (billsUsers.isEmpty()) {
            throw new NotFoundException("No BillsUser found for bill id: " + billId, BillsUser.class);
        }
        for (BillsUser bu : billsUsers) {
            bu = parseToDTO(bu);
        }
        return billsUsers;
    }

    public boolean payBill(Long billId, Long userId) throws NotFoundException {
        BillsUser bu = getByID(userId, billId);
        if (bu != null) {
            if (bu.getOwed().compareTo(bu.getPaid()) > 0) {
                bu.setPaid(bu.getOwed());
                bu.setPaymentDate(LocalDate.now());
                buRepository.save(bu);
                return true;
            }
            throw new NotFoundException("User has already paid the bill", BillsUser.class);
        } else {
            throw new NotFoundException("BillsUser not found with billId: " + billId + " and userId: " + userId, BillsUser.class);
        }
    }

    public List<BillsUser> getPaidBillsByUserId(Long userId) {
        List<BillsUser> billsUsers = buRepository.findByUserPaid(userId);
        if (billsUsers.isEmpty()) {
            throw new NotFoundException("No paid bills found for user id: " + userId, BillsUser.class);
        }
        for (BillsUser bu : billsUsers) {
            bu = parseToDTO(bu);
        }
        return billsUsers;
    }

    public List<BillsUser> getUnpaidBillsByUserId(Long userId) {
        List<BillsUser> billsUsers = buRepository.findByUserNotPaid(userId);
        if (billsUsers.isEmpty()) {
            throw new NotFoundException("No unpaid bills found for user id: " + userId, BillsUser.class);
        }
        for (BillsUser bu : billsUsers) {
            bu = parseToDTO(bu);
        }
        return billsUsers;
    }

    private BillsUser parseToDTO(BillsUser bu) {
        Bill emptyBill = new Bill();
        emptyBill.setId(bu.getBill().getId());
        User emptyUser = new User();
        emptyUser.setId(bu.getUser().getId());
        emptyUser.setName(bu.getUser().getName());
        bu.setBill(emptyBill);
        bu.setUser(emptyUser);
        return bu;
    }
}
