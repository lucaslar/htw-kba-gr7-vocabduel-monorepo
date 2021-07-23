import { Injectable } from '@angular/core';
import { User } from '../model/internal/user';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { SnackbarService } from './snackbar.service';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';
import { RunningGame } from '../model/running-game';
import { tap } from 'rxjs/operators';

@Injectable({
    providedIn: 'root',
})
export class GameService {
    constructor(
        private readonly http: HttpClient,
        private readonly auth: AuthService,
        private readonly snackbar: SnackbarService
    ) {}

    get openGames$(): Observable<RunningGame[]> {
        const url = `${environment.endpointUrl}/game/open-games`;
        return this.http.get<RunningGame[]>(url).pipe(
            tap((r) => {
                return r.sort((a, b) => {
                    return a.finishedRoundsSelf - b.finishedRoundsSelf;
                });
            })
        );
    }

    deleteAccountAndWidows(user: User): void {
        const url = `${environment.endpointUrl}/game/delete-account-and-game-widows`;
        this.http.delete(url).subscribe(() => {
            this.auth.logout();
            this.snackbar.showSnackbar('snackbar.accountDeleted', user);
        });
    }
}
