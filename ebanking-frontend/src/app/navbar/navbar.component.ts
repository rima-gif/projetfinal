import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  isLoggedIn: boolean = false;

  constructor(private router:Router) { }

  ngOnInit(): void {
    // Check if localStorage has a user key (or any key you use to track login)
    this.isLoggedIn = !!localStorage.getItem('Connectuser');
  }

  logout(): void {
    // Clear localStorage and update isLoggedIn state
    localStorage.removeItem('user');
    this.isLoggedIn = false;

    // Optionally, you can redirect the user to the login page
     this.router.navigate(['/home']);
  }
}