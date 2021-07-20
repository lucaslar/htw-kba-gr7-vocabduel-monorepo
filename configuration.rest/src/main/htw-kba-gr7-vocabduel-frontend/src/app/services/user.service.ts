import { Injectable } from '@angular/core';
import { SnackbarService } from './snackbar.service';
import { HttpClient } from '@angular/common/http';
import { AuthService } from './auth.service';
import { User } from '../model/internal/user';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class UserService {
    constructor(
        private readonly http: HttpClient,
        private readonly auth: AuthService,
        private readonly snackbar: SnackbarService
    ) {}

    deleteAccount(user: User): void {
        this.http
            .delete(`${environment.endpointUrl}/user/delete-account`)
            .subscribe(() => {
                this.auth.logout();
                this.snackbar.showSnackbar('snackbar.accountDeleted', user);
            });
    }

    updateAccount$(user: User): Observable<User> {
        const url = `${environment.endpointUrl}/user/update-account`;
        return this.http.put<User>(url, user);
    }

    findUsers$(searchStr: string): Observable<User[]> {
        const url = `${environment.endpointUrl}/user/find`;
        return this.http.get<User[]>(url, { params: { searchStr } });
    }
}
