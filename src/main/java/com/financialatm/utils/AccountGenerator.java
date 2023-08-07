package com.financialatm.utils;

import com.financialatm.api.dto.AccountDTO;
import java.util.Random;


public class AccountGenerator {
    public static AccountDTO generateRandomAccount(String destinationAccount) {


        AccountDTO randomAccount = new AccountDTO();
        randomAccount.setIban(destinationAccount);
        randomAccount.setBalance(5000);
        randomAccount.setBankName("BBVA");

        return randomAccount;
    }
}
