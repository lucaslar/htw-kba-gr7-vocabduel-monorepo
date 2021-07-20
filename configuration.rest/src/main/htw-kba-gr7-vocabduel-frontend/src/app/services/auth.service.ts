import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';
import { JwtHelperService } from '@auth0/angular-jwt';
import { LoginData } from '../model/internal/login-data';
import { StorageService } from './storage.service';
import { LoggedInUser } from '../model/logged-in-user';
import { TokenData } from '../model/token-data';
import { PasswordData } from '../model/internal/password-data';
import { SnackbarService } from './snackbar.service';

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    private readonly user$ = new BehaviorSubject<LoggedInUser | null>(null);

    constructor(
        private readonly http: HttpClient,
        private readonly storage: StorageService,
        private readonly jwtHelper: JwtHelperService,
        private readonly router: Router,
        private readonly snackbar: SnackbarService
    ) {}

    // TODO Implement procedure after registration/login

    login(loginData: LoginData): void {
        const url = `${this.storage.endpointUrl}/auth/login`;
        this.http
            .post<LoggedInUser>(url, loginData)
            .subscribe((result) => this.onSuccessfulAuth(result));
    }

    // TODO: Typify
    register(userData: any): void {
        const url = `${this.storage.endpointUrl}/auth/register`;
        this.http
            .post<LoggedInUser>(url, userData)
            .subscribe((result) => this.onSuccessfulAuth(result));
    }

    get refreshToken$(): Observable<TokenData> {
        const refreshToken = this.storage.refreshToken;
        const url = `${this.storage.endpointUrl}/auth/refresh-token`;
        return this.http.post<TokenData>(url, refreshToken).pipe(
            tap((response) => {
                this.storage.token = response.token;
                this.storage.refreshToken = response.refreshToken;
                this.snackbar.showSnackbar('snackbar.tokenWasRefreshed');
            })
        );
    }

    get currentUser$(): Observable<LoggedInUser | null> {
        return this.user$.pipe(
            switchMap((user) => {
                if (user) return of(user);
                else if (this.storage.token) return this.fetchCurrentUser$;
                else return of(null);
            })
        );
    }

    updatePassword(data: PasswordData): void {
        const url = `${this.storage.endpointUrl}/auth/update-password`;
        this.http.put<TokenData>(url, data).subscribe(() => {
            data.currentPassword = '';
            data.newPassword = '';
            data.confirm = '';
            this.snackbar.showSnackbar('snackbar.passwordUpdated');
        });
    }

    logout(): void {
        this.storage.token = null;
        this.storage.refreshToken = null;
        this.router.navigate(['login']).then();
        this.user$.next(null);
    }

    private get fetchCurrentUser$(): Observable<LoggedInUser> {
        return this.http
            .get<LoggedInUser>(`${this.storage.endpointUrl}/auth/current-user`)
            .pipe(
                tap((user) => {
                    if (!user) this.storage.token = null;
                    this.user$.next(user);
                })
            );
    }

    private onSuccessfulAuth(result: LoggedInUser): void {
        this.storage.token = result.authTokens.token;
        this.storage.refreshToken = result.authTokens.refreshToken;
        this.user$.next(result);
        this.router.navigate(['dashboard']).then();
    }
}
