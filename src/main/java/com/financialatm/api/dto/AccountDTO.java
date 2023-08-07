package com.financialatm.api.dto;
import javax.validation.constraints.Pattern;

public class AccountDTO {

    public static final String IBAN_PATTERN = "/^ES[0-9]{2}[0-9]{20}$/gm";

    @Pattern(regexp = IBAN_PATTERN, message = "Invalid IBAN format")
    private String iban;
    private double balance;

    private String bankName;

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }


    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
