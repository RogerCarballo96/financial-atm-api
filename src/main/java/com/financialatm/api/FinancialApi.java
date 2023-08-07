package com.financialatm.api;

import com.financialatm.api.dto.CardDTO;
import com.financialatm.api.dto.MovementDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atm/financial")
public interface FinancialApi {

    @GetMapping("/{iban}")
    ResponseEntity<List<MovementDTO>> getAllMovementsByIban(@PathVariable String iban);

    @PostMapping("/api/atm/withdrawal")
    ResponseEntity<String> withdraw(@RequestBody CardDTO card, @RequestParam double amount, @RequestParam String bank);


}
