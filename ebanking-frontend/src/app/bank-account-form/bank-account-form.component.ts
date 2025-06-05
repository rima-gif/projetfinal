import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CustomerService } from '../services/customer.service';

@Component({
  selector: 'app-bank-account-form',
  templateUrl: './bank-account-form.component.html',
  styleUrls: ['./bank-account-form.component.css']
})
export class BankAccountFormComponent {
  accountForm: FormGroup;
  

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.accountForm = this.fb.group({
      acountnumber: ['', Validators.required],
      balance: [0, Validators.required],
      status: ['CREATED', Validators.required],
      //customerId: [7, Validators.required] // ID du customer existant
    });
  }

  onSubmit() {
    const formValue = this.accountForm.value;
    const generatedAccountNumber = this.generateRandomString(15);
    const customer = JSON.parse(localStorage.getItem('Connectuser')!);
    console.log('Customer from localStorage:', customer); // Log the full customer object

    if (!customer || !customer.id) {
        console.error('Invalid customer data');
        alert('User not logged in or invalid customer data');
        return;
    }

    const newAccount = {
        accountNumber: generatedAccountNumber,
        balance: formValue.balance,
        status: formValue.status,
        customer: customer
    };
    console.log('Payload:', newAccount);

    this.http.post('http://localhost:9054/bankaccount/account/add', newAccount)
        .subscribe({
            next: response => {
                console.log("Compte créé :", response);
                alert("Compte bancaire créé avec succès !");
            },
            error: err => {
                console.log('Payload sent:', newAccount);
                console.error("Erreur lors de la création :", err);
                alert("Erreur lors de la création du compte: " + (err.error || "Erreur serveur"));
            }
        });
}
  
  // Fonction pour générer une chaîne aléatoire de 15 caractères
  generateRandomString(length: number): string {
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    let result = '';
    for (let i = 0; i < length; i++) {
      result += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return result;
  }
  
}
