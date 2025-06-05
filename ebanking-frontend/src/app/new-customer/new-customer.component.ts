import { Component, OnInit } from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Customer} from "../model/customer.model";
import {CustomerService} from "../services/customer.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-new-customer',
  templateUrl: './new-customer.component.html',
  styleUrls: ['./new-customer.component.css']
})
export class NewCustomerComponent implements OnInit {
  newCustomerFormGroup! : FormGroup;
  constructor(private fb : FormBuilder,private customerService:CustomerService,private router:Router) { }

  ngOnInit(): void {
    this.newCustomerFormGroup=this.fb.group({
       name : this.fb.control(null,[Validators.required,Validators.minLength(4)]),
       email : this.fb.control(null,[Validators.required,Validators.email]),
       password : this.fb.control(null,[Validators.required]),
       confirmPassword: ['', Validators.required],
     },
     {
       validators: this.passwordsMatchValidator
     }
     );
     
  }
  
  passwordsMatchValidator(form: AbstractControl) {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
    }
  handelSaveCustomer() {
     let customer:Customer=this.newCustomerFormGroup.value;
     this.customerService.saveCustomer(customer).subscribe({
       next : data =>{
         alert("Customer has been successfuly saved!!");
         this.router.navigateByUrl("/customers");
       },
       error : err => {
         console.log(err);
       }
     });
  }
}
