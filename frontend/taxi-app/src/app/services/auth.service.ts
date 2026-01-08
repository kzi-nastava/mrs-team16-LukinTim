import { Injectable } from '@angular/core';
import { UserRegisterDto, UserLoginDto, LoginResponseDto } from '../types/auth';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, tap } from 'rxjs';
import { Router } from '@angular/router';
import { jwtDecode, JwtPayload } from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8080/auth';

  public isLoggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(
    false
  );

  constructor(private httpClient: HttpClient, private router: Router) {
    const token = localStorage.getItem('accessToken');
    this.isLoggedIn.next(!!token && this.isTokenValid(token));
  }

  confirmAccount(token: string) {
    return this.httpClient.get(`${this.API_URL}/confirm-registration/${token}`);
  }

  submitRegister(user: UserRegisterDto) {
    console.log('Sending registration request for', user.email);
    return this.httpClient.post(`${this.API_URL}/register`, user);
  }

  submitLogin(user: UserLoginDto) {
    return this.httpClient
      .post<LoginResponseDto>(`${this.API_URL}/login`, user)
      .pipe(
        tap((response) => {
          localStorage.setItem('accessToken', response.accessToken);

          localStorage.setItem(
            'user',
            JSON.stringify({
              email: response.email,
              firstname: response.firstname,
              lastname: response.lastname,
            })
          );

          this.isLoggedIn.next(true);
        })
      );
  }

  logout() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('user');
    this.isLoggedIn.next(false);
    this.router.navigate(['/login']);
  }

  getUser() {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }

  getAccessToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  private isTokenValid(token: string): boolean {
    try {
      const decoded = jwtDecode<JwtPayload>(token);
      const now = Date.now() / 1000;
      return decoded.exp !== undefined && decoded.exp > now;
    } catch {
      return false;
    }
  }
}
