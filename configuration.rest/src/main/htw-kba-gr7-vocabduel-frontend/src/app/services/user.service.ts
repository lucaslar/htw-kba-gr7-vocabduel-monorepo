import { Injectable } from '@angular/core';
import { SnackbarService } from './snackbar.service';
import { HttpClient } from '@angular/common/http';
import { AuthService } from './auth.service';
import { StorageService } from './storage.service';
import { User } from '../model/internal/user';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class UserService {
    constructor(
        private readonly http: HttpClient,
        private readonly auth: AuthService,
        private readonly storage: StorageService,
        private readonly snackbar: SnackbarService
    ) {}

    deleteAccount(user: User): void {
        this.http
            .delete(`${this.storage.endpointUrl}/user/delete-account`)
            .subscribe(() => {
                this.auth.logout();
                this.snackbar.showSnackbar('snackbar.accountDeleted', user);
            });
    }

    updateAccount$(user: User): Observable<User> {
        return this.http.put<User>(
            `${this.storage.endpointUrl}/user/update-account`,
            user
        );
    }

    findUsers$(searchStr: string): Observable<User[]> {
        return this.http.get<User[]>(`${this.storage.endpointUrl}/user/find`, {
            params: { searchStr },
        });
    }
}
