import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root',
})
export class StorageService {
    private readonly authTokenKey = 'authToken';
    private readonly refreshTokenKey = 'refreshToken';

    private _token: string | null = null;
    private _refreshToken: string | null = null;

    get token(): string | null {
        if (!this._token) {
            this._token = sessionStorage.getItem(this.authTokenKey);
        }
        return this._token;
    }

    set token(value: string | null) {
        this._token = value;
        if (value) sessionStorage.setItem(this.authTokenKey, value);
        else sessionStorage.removeItem(this.authTokenKey);
    }

    get refreshToken(): string | null {
        if (!this._refreshToken) {
            this._refreshToken = sessionStorage.getItem(this.refreshTokenKey);
        }
        return this._refreshToken;
    }

    set refreshToken(value: string | null) {
        this._refreshToken = value;
        if (value) sessionStorage.setItem(this.refreshTokenKey, value);
        else sessionStorage.removeItem(this.refreshTokenKey);
    }
}
