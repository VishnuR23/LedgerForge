
package com.vraj.ledgerforge.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class Routes {
    private final RestClient cmd = RestClient.create("http://localhost:8081");
    private final RestClient qry = RestClient.create("http://localhost:8082");

    @PostMapping("/accounts/{id}/credit")
    public ResponseEntity<?> credit(@PathVariable String id, @RequestBody Map<String,Object> body){
        var amt = ((Number)body.getOrDefault("amountCents",0)).longValue();
        return cmd.post().uri("/accounts/credit").body(Map.of("accountId", id, "amountCents", amt)).retrieve().toEntity(Object.class);
    }

    @PostMapping("/accounts/{id}/debit")
    public ResponseEntity<?> debit(@PathVariable String id, @RequestBody Map<String,Object> body){
        var amt = ((Number)body.getOrDefault("amountCents",0)).longValue();
        return cmd.post().uri("/accounts/debit").body(Map.of("accountId", id, "amountCents", amt)).retrieve().toEntity(Object.class);
    }

    @GetMapping("/accounts/{id}")
    public Object balance(@PathVariable String id){
        return qry.get().uri("/balances/{id}", id).retrieve().body(Object.class);
    }
}
