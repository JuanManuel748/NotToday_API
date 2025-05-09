package com.github.juanmanuel.nottodaytomorrow.controllers;

import com.github.juanmanuel.nottodaytomorrow.models.Bill;
import com.github.juanmanuel.nottodaytomorrow.services.BillService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/bills")
public class BillController {
    @Autowired
    private BillService billService;

    @Operation(summary = "Crea una factura")
    @PostMapping
    public ResponseEntity<Bill> create(@RequestBody Bill bill) {
        Bill createdBill = billService.create(bill);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBill);
    }

    @Operation(summary = "Actualiza una factura")
    @PutMapping("/{id}")
    public ResponseEntity<Bill> update(@PathVariable Long id, @RequestBody Bill bill) {
        Bill updatedBill = billService.update(id, bill);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBill);
    }

    @Operation(summary = "Elimina una factura")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (billService.delete(id)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Busca una factura por su id")
    @GetMapping("/{id}")
    public ResponseEntity<Bill> getById(@PathVariable Long id) {
        Bill bill = billService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(bill);
    }

    @Operation(summary = "Busca todas las facturas")
    @GetMapping
    public ResponseEntity<List<Bill>> getAll() {
        List<Bill> bills = billService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(bills);
    }

    @Operation(summary = "Busca facturas por su monto")
    @GetMapping("/amount/{amount}")
    public ResponseEntity<List<Bill>> getByAmount(@PathVariable BigDecimal maxmount, @RequestParam(required = false) BigDecimal amount) {
        BigDecimal min = amount != null ? amount : BigDecimal.ZERO;
        List<Bill> bills = billService.findByAmountRange(min, maxmount);
        return ResponseEntity.status(HttpStatus.OK).body(bills);
    }

    @Operation(summary = "Busca facturas por su descripción")
    @GetMapping("/description")
    public ResponseEntity<List<Bill>> getByDescription(@RequestParam String description) {
        List<Bill> bills = billService.findByDescription(description);
        return ResponseEntity.status(HttpStatus.OK).body(bills);
    }

    @Operation(summary = "Busca facturas por su grupo")
    @GetMapping("/teams/{teamId}")
    public ResponseEntity<List<Bill>> getByTeamId(@PathVariable Long teamId) {
        List<Bill> bills = billService.findByTeam(teamId);
        return ResponseEntity.status(HttpStatus.OK).body(bills);
    }

    @Operation(summary = "Busca facturas por su creador")
    @GetMapping("/payer/{payerId}")
    public ResponseEntity<List<Bill>> getByPayerId(@PathVariable Long payerId) {
        List<Bill> bills = billService.findByPayer(payerId);
        return ResponseEntity.status(HttpStatus.OK).body(bills);
    }


}
