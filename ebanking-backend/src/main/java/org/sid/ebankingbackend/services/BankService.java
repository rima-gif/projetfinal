package org.sid.ebankingbackend.services;

import jakarta.transaction.Transactional;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
@Transactional
public class BankService {
    private static final Logger logger = LoggerFactory.getLogger(BankService.class);

    @Autowired
    private BankAccountRepository bankAccountRepository;

    public void consulter(){
        bankAccountRepository.findById("0cd0b20c-b765-4cea-90bf-564da85deae8").ifPresent(this::afficherDetailsCompte);
    }

    private void afficherDetailsCompte(BankAccount bankAccount) {
        logger.info("***********************");
        logger.info("ID: {}", bankAccount.getId());
        logger.info("Balance: {}", bankAccount.getBalance());
        logger.info("Status: {}", bankAccount.getStatus());
        logger.info("Created At: {}", bankAccount.getCreateAt());
        logger.info("Customer: {}", bankAccount.getCustomer().getName());
        logger.info("Account Type: {}", bankAccount.getClass().getSimpleName());

        if (bankAccount instanceof CurrentAccount currentAccount) {
            logger.info("Over Draft: {}", currentAccount.getOverDraft());
        } else if (bankAccount instanceof SavingAccount savingAccount) {
            logger.info("Rate: {}", savingAccount.getInterestRate());
        }

        bankAccount.getAccountOperations().forEach(op -> {
            logger.info("Operation: {} {} {}", op.getType(), op.getOperationDate(), op.getAmount());
        });
        logger.info("============================");
    }
}
