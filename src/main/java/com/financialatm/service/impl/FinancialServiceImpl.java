package com.financialatm.service.impl;

import com.financialatm.api.dto.CardDTO;
import com.financialatm.api.dto.MovementDTO;
import com.financialatm.service.FinancialService;
import com.financialatm.utils.MovementGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialServiceImpl implements FinancialService {
    @Override
    public List<MovementDTO> getAllMovementsByIban(String iban) {
        int numberOfMovements = 15;
        List<MovementDTO> randomMovements = MovementGenerator.generateRandomMovements(numberOfMovements);

        return randomMovements.stream()
                .filter(movement -> movement.getSourceAccount().equals(iban))
                .toList();
    }

    @Override
    public String withdraw(CardDTO card, Double amount, String cashMachineBank) {

        if(!card.isActive()) {
            throw new RuntimeException("Tu tarjeta no está activa actualmente");
        }

        double moneyToWithdraw = 0;
        if ("debit".equals(card.getCardType())) {
            double availableBalance = card.getAccount().getBalance();
            if (availableBalance <= 0) {
                throw new RuntimeException("No dispones de saldo suficiente");
            } else if (amount > availableBalance) {
                throw new RuntimeException("No se te permite retirar más dinero del que tienes");
            } else if(amount > card.getCreditLimit()) {
                throw new RuntimeException("No puedes sacar más dinero del máximo de crédito disponible");
            }
        } else if ("credit".equals(card.getCardType())) {
            double creditLimit = card.getCreditLimit();
            if (amount > creditLimit) {
                throw new RuntimeException("No puedes sacar más dinero del máximo de crédito disponible");
            }
        } else {
            throw new RuntimeException("Tipo de tarjeta no válido");
        }
        String commisionText = "";
        if(!cashMachineBank.equals(card.getAccount().getBankName())) {
            double commisionPercentage = 0.05;
            double commission = calculateCommission(amount, commisionPercentage);
            moneyToWithdraw = amount - commission;
            commisionText = "Se te ha aplicado un " + commisionPercentage + "% de comisión por ser de otro banco.";
        }

        card.getAccount().setBalance(card.getAccount().getBalance() - amount);
        return "Has retirado " + moneyToWithdraw + " del banco. Tu saldo actual es: " + card.getAccount().getBalance() + ". " + commisionText;
    }

    private double calculateCommission(double amount, double commisionPercentage) {
        return amount * commisionPercentage;
    }
}
