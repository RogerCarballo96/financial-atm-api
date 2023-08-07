package com.financialatm.service.impl;

import com.financialatm.api.dto.AccountDTO;
import com.financialatm.api.dto.CardDTO;
import com.financialatm.api.dto.MovementDTO;
import com.financialatm.service.FinancialService;
import com.financialatm.utils.AccountGenerator;
import com.financialatm.utils.MovementGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialServiceImpl implements FinancialService {
    @Override
    public List<MovementDTO> getAllMovementsByIban(String iban) {

        //Aqui haríamos una consulta mediante jpa para obtener los movimientos a partir del IBAN.
        int numberOfMovements = 15;
        List<MovementDTO> randomMovements = MovementGenerator.generateRandomMovements(numberOfMovements);

        return randomMovements.stream()
                .filter(movement -> movement.getSourceAccount().equals(iban))
                .toList();
    }

    @Override
    public String withdraw(CardDTO card, Double amount, String cashMachineBank, String iban) {

        if(!card.isActive()) {
            throw new RuntimeException("Tu tarjeta no está activa actualmente");
        }

        if(!card.getAccount().getIban().equals(iban)) {
            throw new RuntimeException("Tu tarjeta no está asociada con el iban al que quieres retirar el dinero");
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
            double commissionPercentage = 0.05;
            double commission = calculateCommission(amount, commissionPercentage);
            moneyToWithdraw = amount - commission;
            commisionText = "Se te ha aplicado un " + commissionPercentage + "% de comisión por ser de otro banco.";
        }

        // Aqui llamariamos a un repository para udpatear la tabla de la cuenta bancaria
        // y creariamos un nuevo registro en la tabla de movimientos mediante JPA.
        card.getAccount().setBalance(card.getAccount().getBalance() - amount);
        return "Has retirado " + moneyToWithdraw + " del banco. Tu saldo actual es: " + card.getAccount().getBalance() + ". " + commisionText;
    }

    @Override
    public String deposit(CardDTO card, Double amount, String bank, String iban) {

        if(!card.isActive()) {
            throw new RuntimeException("Tu tarjeta no está activa actualmente");
        }

        if(!iban.equals(card.getAccount().getIban())){
            throw new RuntimeException("IBAN no asociado con tarjeta");
        }

        if(!bank.equals(card.getAccount().getBankName())) {
            throw new RuntimeException("No puedes ingresar dinero a otras entidades");
        }
        // Aqui llamariamos a un repository para udpatear la tabla de la cuenta bancaria
        // y creariamos un nuevo registro en la tabla de movimientos mediante JPA.
        card.getAccount().setBalance(card.getAccount().getBalance() + amount);

        return "Has ingresado " + amount + " a tu cuenta. El saldo actual es " + card.getAccount().getBalance();
    }

    @Override
    public String transferMoney(CardDTO card, Double amount, String destinationAccount) {

        if(card.getAccount().getIban().equals(destinationAccount)) {
            throw new RuntimeException("No puedes hacer transferencias a tu misma cuenta");
        }

        if(card.getAccount().getBalance() < amount) {
            throw new RuntimeException("No dispones de saldo suficiente");
        }
        //Aqui hariamos mediante repository(jpa), una consulta para obtener los datos del destinationAccount
        AccountDTO destinationAccountDTO = AccountGenerator.generateRandomAccount(destinationAccount);
        String commisionText = "";

        if(card.getAccount().getBankName().equals(destinationAccountDTO.getBankName())) {
            destinationAccountDTO.setBalance(destinationAccountDTO.getBalance() + amount);
            card.getAccount().setBalance(card.getAccount().getBalance() - amount);
        } else {
            double commisionPercentage = 0.05;
            double commissionAmount = calculateCommission(amount, commisionPercentage);
            double amountToDiscount = commissionAmount + amount;

            if(card.getAccount().getBalance() < amountToDiscount) {
                throw new RuntimeException("No dispones de saldo suficiente para la transferencia debido a que te estamos aplicando uan comisión");
            }
            //Aqui hariamos mediante repository(jpa), los updates pertinentes
            destinationAccountDTO.setBalance(destinationAccountDTO.getBalance() + amount);
            card.getAccount().setBalance(card.getAccount().getBalance() - amountToDiscount);
            commisionText = "Se te ha aplicado una comisión del " + commisionPercentage + "%.";
        }


        return "Se ha transferido correctamente el dinero a la cuenta destino. Tu actual balance es: " + card.getAccount().getBalance() + ". " + commisionText;
    }

    private double calculateCommission(double amount, double commisionPercentage) {
        return amount * commisionPercentage;
    }
}
