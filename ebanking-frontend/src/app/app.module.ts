import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { CustomersComponent } from './customers/customers.component';
import { AccountsComponent } from './accounts/accounts.component';
import {HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from "@angular/forms";
import { NewCustomerComponent } from './new-customer/new-customer.component';
import { CustomerAccountsComponent } from './customer-accounts/customer-accounts.component';
import { FooterComponent } from './footer/footer.component';
import { LoginComponent } from './login/login.component';
import { BankAccountFormComponent } from './bank-account-form/bank-account-form.component';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { BankAccountListComponent } from './bank-account-list/bank-account-list.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    CustomersComponent,
    AccountsComponent,
    NewCustomerComponent,
    CustomerAccountsComponent,
    LoginComponent,
    BankAccountFormComponent,
    HomeComponent,
    BankAccountListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    CommonModule
    
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
