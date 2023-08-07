package com.financialatm.controller;

import com.financialatm.api.FinancialApi;
import com.financialatm.api.dto.CardDTO;
import com.financialatm.api.dto.MovementDTO;
import com.financialatm.service.FinancialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FinancialControllerApi implements FinancialApi {

    private final FinancialService financialService;

    @Autowired
    public FinancialControllerApi(FinancialService financialService) {
        this.financialService = financialService;
    }

    @Override
    public ResponseEntity<List<MovementDTO>> getAllMovementsByIban(String iban) {
        return ResponseEntity.ok(financialService.getAllMovementsByIban(iban));
    }

    @Override
    public ResponseEntity<String> withdraw(CardDTO card, double amount, String bank , String iban) {
        try {
            return ResponseEntity.ok(financialService.withdraw(card, amount, bank, iban));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> depositMoney(CardDTO card, double amount, String bank, String ibanToDeposit) {
        try {
            return ResponseEntity.ok(financialService.deposit(card, amount, bank, ibanToDeposit));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
