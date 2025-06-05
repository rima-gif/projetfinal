import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CustomersComponent} from "./customers/customers.component";
import {AccountsComponent} from "./accounts/accounts.component";
import {NewCustomerComponent} from "./new-customer/new-customer.component";
import {CustomerAccountsComponent} from "./customer-accounts/customer-accounts.component";
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import{BankAccountFormComponent} from "./bank-account-form/bank-account-form.component";
import {BankAccountListComponent} from "./bank-account-list/bank-account-list.component";

const routes: Routes = [
  {path : "customers",component : CustomersComponent},
  {path : "bank-account-list",component : BankAccountListComponent},
  {path:"bank-account-form",component : BankAccountFormComponent},
  {path:"home",component : HomeComponent},
  {path : "login",component : LoginComponent},
  {path : "new-customer",component : NewCustomerComponent},
  {path : "customer-accounts/:id",component : CustomerAccountsComponent},
  {path : "accounts",component : AccountsComponent},
  
  {path : "",redirectTo : "home",pathMatch : "full"},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
