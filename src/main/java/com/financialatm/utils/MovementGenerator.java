package com.financialatm.utils;

import com.financialatm.api.dto.MovementDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MovementGenerator {

    public static List<MovementDTO> generateRandomMovements(int numberOfMovements) {
        List<MovementDTO> movementsList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numberOfMovements; i++) {
            MovementDTO movement = new MovementDTO();
            movement.setId((long) (i + 1));
            movement.setType(getRandomMovementType());
            movement.setDescription("Description " + (i + 1));
            movement.setAmount(BigDecimal.valueOf(random.nextDouble() * 1000));
            movement.setDate(String.valueOf(LocalDateTime.now().minusDays(random.nextInt(365))));
            movement.setSourceAccount(getRandomSourceAccount());
            movement.setDestinationAccount("DestinationAccount-" + (i + 1));
            movementsList.add(movement);
        }

        return movementsList;
    }

    private static String getRandomMovementType() {
        String[] movementTypes = {"ingreso", "retirada", "comisión", "transferencia"};
        Random random = new Random();
        int index = random.nextInt(movementTypes.length);
        return movementTypes[index];
    }

    private static String getRandomSourceAccount() {
        Random random = new Random();
        String[] ibanList = {"ES6920805779387558994453", "ES1021009328486534446962", "ES8104873236379691495542", "ES5300759931966836897257"};
        int index = random.nextInt(ibanList.length);
        return ibanList[index];
    }
}
