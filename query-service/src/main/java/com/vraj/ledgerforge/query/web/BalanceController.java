
package com.vraj.ledgerforge.query.web;

import com.vraj.ledgerforge.query.readmodel.AccountBalanceRepo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/balances")
public class BalanceController {
    private final AccountBalanceRepo repo;
    public BalanceController(AccountBalanceRepo repo){ this.repo = repo; }

    @GetMapping("/{accountId}")
    public Object get(@PathVariable String accountId){
        return repo.findById(accountId).orElse(null);
    }
}
