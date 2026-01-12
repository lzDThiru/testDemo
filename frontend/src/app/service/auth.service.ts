import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    private readonly API_URL = '/api/auth';

    constructor(private http: HttpClient) {}

    register(username: string, password: string): Observable<any> {
        const payload = { username, password };
        return this.http.post(`${this.API_URL}/register`, payload);
    }

    login(username: string, password: string): Observable<any> {
        const payload = { username, password };
        return this.http.post(`${this.API_URL}/login`, payload);
    }

    isAuthenticated(): boolean {
        const token = localStorage.getItem('token');
        if (!token) {
            return false;
        }

        // Optionally, validate the token (e.g., check expiration)
        const payload = JSON.parse(atob(token.split('.')[1])); // Decode JWT payload
        const isExpired = Date.now() > payload.exp * 1000; // Check expiration
        return !isExpired;
    }
}