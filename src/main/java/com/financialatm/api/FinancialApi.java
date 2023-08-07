package com.financialatm.api;

import com.financialatm.api.dto.AccountDTO;
import com.financialatm.api.dto.CardDTO;
import com.financialatm.api.dto.MovementDTO;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atm/financial")
public interface FinancialApi {

    @GetMapping("/{iban}")
    ResponseEntity<List<MovementDTO>> getAllMovementsByIban(@PathVariable String iban);

    @PostMapping("withdraw")
    ResponseEntity<String> withdraw(@RequestBody CardDTO card,
                                    @RequestParam double amount,
                                    @RequestParam String bank,
                                    @RequestParam String iban);

    @PostMapping("/deposit")
    ResponseEntity<String> depositMoney(@RequestBody CardDTO card,
                                        @RequestParam double amount,
                                        @RequestParam String bank,
                                        @RequestParam String ibanToDeposit);

    @PostMapping("/transfer")
    ResponseEntity<String> transferMoney(@RequestBody CardDTO card,
                                         @RequestParam String destinationAccount,
                                         @RequestParam double amount);


}
