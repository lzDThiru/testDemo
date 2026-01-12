import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError } from 'rxjs';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private http: HttpClient, private router: Router, private authService: AuthService) {}

  onSubmit() {
    const payload = { username: this.username, password: this.password };
  this.authService.login(this.username, this.password).subscribe(
    (response: any) => {
      localStorage.setItem('token', response.token); // Store JWT token
      this.router.navigate(['/tasks']); // Navigate to tasks page
    },
    (error: any) => {
      this.errorMessage = error.error.message || 'Login failed. Please try again.';
      console.error('Login failed', error);
    }
  );
  }
}
