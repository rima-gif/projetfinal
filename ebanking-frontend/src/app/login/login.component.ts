import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomerService } from '../services/customer.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']

})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: CustomerService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  onLogin() {
    if (this.loginForm.valid) {
      const credentials = this.loginForm.value;
      this.authService.login(credentials.email, credentials.password).subscribe(
        (user: any) => {
          // ✅ Save user id to localStorage
          localStorage.setItem('userId', user.id);
          localStorage.setItem('Connectuser', JSON.stringify(user));
          console.log('User ID saved:', user.id);

          // Redirect or show success
          this.router.navigate(['/home']);
        },
        (error) => {
          console.error('Login failed:', error);
        }
      );
    }
  }
}
