package org.sid.ebankingbackend.web;

import lombok.AllArgsConstructor;
import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.exceptions.BalanceNotSufficientException;
import org.sid.ebankingbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.services.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class BankAccountRestAPI {
    private final BankAccountRepository bankAccountRepository;
    private BankAccountService bankAccountService;

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts() {
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(@PathVariable String accountId,
                                               @RequestParam(name = "page", defaultValue = "0") int page,
                                               @RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId, page, size);
    }

    @GetMapping("/accounts/{idAccount}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String idAccount) {
        return bankAccountService.accountHistory(idAccount);
    }

    @PostMapping("/account/add")
    public ResponseEntity<?> addAccount(@RequestBody BankAccount account) {
        try {
            BankAccount saved = bankAccountService.saveBankAccount(account);
            return ResponseEntity.ok(saved);
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Customer not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating account: " + e.getMessage());
        }
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<?> getAccountsByCustomer(@PathVariable Long id) {
        try {
            List<BankAccountDTO> accounts = bankAccountService.getAccountsByCustomer(id);

            return ResponseEntity.ok(accounts);
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Customer not found: " + e.getMessage());
        }
    }

    @PostMapping("/accounts/debit")
    public ResponseEntity<?> debitAccount(@RequestBody DebitDTO debitRequest) {
        try {
            bankAccountService.debit(debitRequest.getAccountId(), debitRequest.getAmount(), debitRequest.getDescription());
            return ResponseEntity.ok("Account debited successfully");
        } catch (BankAccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Bank account not found: " + e.getMessage());
        } catch (BalanceNotSufficientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Insufficient balance: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error debiting account: " + e.getMessage());
        }
    }

    @PostMapping("/accounts/credit")
    public ResponseEntity<?> creditAccount(@RequestBody CreditDTO creditRequest) {
        try {
            bankAccountService.credit(creditRequest.getAccountId(), creditRequest.getAmount(), creditRequest.getDescription());
            return ResponseEntity.ok("Account credited successfully");
        } catch (BankAccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Bank account not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error crediting account: " + e.getMessage());
        }
    }

    @PostMapping("/accounts/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequestDTO transferRequest) {
        try {
            bankAccountService.transfer(
                    transferRequest.getAccountSource(),
                    transferRequest.getAccountDestination(),
                    transferRequest.getAmount(),
                    transferRequest.getDescription()
            );
            return ResponseEntity.ok("Transfer completed successfully");
        } catch (BankAccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Bank account not found: " + e.getMessage());
        } catch (BalanceNotSufficientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Insufficient balance: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing transfer: " + e.getMessage());
        }
    }
}
