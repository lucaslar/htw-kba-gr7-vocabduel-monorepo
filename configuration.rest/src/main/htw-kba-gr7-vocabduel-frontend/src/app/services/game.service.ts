import { Injectable } from '@angular/core';
import { User } from '../model/internal/user';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { SnackbarService } from './snackbar.service';
import { AuthService } from './auth.service';

@Injectable({
    providedIn: 'root',
})
export class GameService {
    constructor(
        private readonly http: HttpClient,
        private readonly auth: AuthService,
        private readonly snackbar: SnackbarService
    ) {}

    deleteAccountAndWidows(user: User): void {
        this.http
            .delete(`${environment.endpointUrl}/game/delete-account-and-game-widows`)
            .subscribe(() => {
                this.auth.logout();
                this.snackbar.showSnackbar('snackbar.accountDeleted', user);
            });
    }
}
