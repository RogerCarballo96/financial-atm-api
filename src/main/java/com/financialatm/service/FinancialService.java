package com.financialatm.service;

import com.financialatm.api.dto.CardDTO;
import com.financialatm.api.dto.MovementDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FinancialService {
    List<MovementDTO> getAllMovementsByIban(String iban);
    String withdraw(CardDTO card, Double amount, String bank, String iban);
    String deposit(CardDTO card, Double amount, String bank, String iban);
}
