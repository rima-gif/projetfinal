package org.sid.ebankingbackend.services;

import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.exceptions.BalanceNotSufficientException;
import org.sid.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    List<CustomerDTO> listCustomers();
    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(Long customerId);
    List<CustomerDTO> searchCustomers(String keyword);
    public BankAccount saveBankAccount(BankAccount account) throws CustomerNotFoundException ;

    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    List<BankAccountDTO> bankAccountList();
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource, String accountIdDestination, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    List<AccountOperationDTO> accountHistory(String accountId);
    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
    boolean checkLogin(String email, String password);
    CustomerDTO login(String email, String password);
    List<BankAccountDTO> getAccountsByCustomer(Long customerId) throws CustomerNotFoundException;
}