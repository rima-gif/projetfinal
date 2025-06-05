import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  isLoggedIn: boolean = false;

  ngOnInit(): void {
    // Check if localStorage has a user key (or any key you use to track login)
    this.isLoggedIn = !!localStorage.getItem('Connectuser');
  }
}