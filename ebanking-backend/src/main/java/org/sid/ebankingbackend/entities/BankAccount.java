package org.sid.ebankingbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.ebankingbackend.entities.AccountOperation;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.enums.AccountStatus;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 20)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private double balance;
    private Date createAt;
    private String accountNumber;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY)
    private List<AccountOperation> accountOperations;

    @PrePersist
    public void prePersist() {
        if (createAt == null) {
            createAt = new Date();
        }
    }
}