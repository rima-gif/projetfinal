package org.sid.ebankingbackend.repositories;

import org.sid.ebankingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {

    List<BankAccount> findByCustomerId(Long customerId);
}
