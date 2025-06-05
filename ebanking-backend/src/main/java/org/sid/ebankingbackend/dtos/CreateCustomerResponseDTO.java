package org.sid.ebankingbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerResponseDTO {
    private CustomerDTO customerDTO;
    private CurrentBankAccountDTO accountDTO;
}