package com.github.juanmanuel.nottodaytomorrow.services;

import com.github.juanmanuel.nottodaytomorrow.exceptions.NotFoundException;
import com.github.juanmanuel.nottodaytomorrow.models.Bill;
import com.github.juanmanuel.nottodaytomorrow.models.BillsUser;
import com.github.juanmanuel.nottodaytomorrow.models.User;
import com.github.juanmanuel.nottodaytomorrow.repositories.BillsUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BillsUserService {
    @Autowired
    private BillsUserRepository buRepository;
    @Autowired
    private TeamService teamService;
    @Autowired
    private UserService userService;



    public List<BillsUser> create(Bill b) throws NotFoundException {
        List<BillsUser> resultList = new ArrayList<>();
        List<User> users = teamService.getUsersByTeamId(b.getTeam().getId());
        BigDecimal amountperUser = b.getAmount().divide(new BigDecimal(users.size()), BigDecimal.ROUND_HALF_UP);
        for (User u: users) {
            BillsUser bu = new BillsUser(b, u, amountperUser, new BigDecimal("0.0"));
            if (u.equals(b.getPayer())) {
                bu.setPaid(amountperUser);
            }
            buRepository.save(bu);
            resultList.add(bu);
        }
        return resultList;
    }

    public BillsUser getByID(Long billId, Long userId) throws NotFoundException {
        Optional<BillsUser> bu = buRepository.findByPK(billId, userId);
        if (bu.isPresent()) {
            return bu.get();
        }
        throw new NotFoundException("BillsUser not found with billId: " + billId + " and userId: " + userId, BillsUser.class);
    }

    public List<BillsUser> getAll() throws NotFoundException {
        List<BillsUser> billsUsers = buRepository.findAll();
        if (billsUsers.isEmpty()) {
            throw new NotFoundException("No bills found", BillsUser.class);
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
}
