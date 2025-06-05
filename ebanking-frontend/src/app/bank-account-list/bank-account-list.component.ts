import { Component, OnInit } from '@angular/core';
import { AccountsService } from '../services/accounts.service';

@Component({
  selector: 'app-bank-account-list',
  templateUrl: './bank-account-list.component.html',
  styleUrls: ['./bank-account-list.component.css']
})
export class BankAccountListComponent implements OnInit {
  accounts: any[] = [];

  constructor(private bankAccountService: AccountsService) {}

  ngOnInit() {
    const userId = localStorage.getItem('userId');
    const customerId = userId ? Number(userId) : null;
    console.log("Stored userId:", userId);
    console.log("Parsed customerId:", customerId);

    if (customerId) {
      this.bankAccountService.getAccountsByCustomer(customerId).subscribe({
        next: (data) => {
          console.log("Raw API response:", data);
          this.accounts = data.map(account => ({
            id: account.id, // Match the template's expected property
            balance: account.balance,
            type: account.type
          }));
          console.log("Mapped accounts:", this.accounts);
        },
        error: (err) => {
          console.error("Error while retrieving accounts:", err);
        }
      });
    } else {
      console.error("No customerId found in localStorage. Please log in first.");
    }
  }
}