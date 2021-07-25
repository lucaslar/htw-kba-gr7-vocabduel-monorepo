import { Injectable } from '@angular/core';
import {
    Resolve,
    RouterStateSnapshot,
    ActivatedRouteSnapshot,
    Router,
} from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { GameService } from '../services/game.service';
import { VocabduelRound } from '../model/vocabduel-round';
import { SnackbarService } from '../services/snackbar.service';

@Injectable({
    providedIn: 'root',
})
export class GameAccessResolver implements Resolve<VocabduelRound | boolean> {
    constructor(
        private readonly game: GameService,
        private readonly router: Router,
        private readonly snackbar: SnackbarService
    ) {}

    resolve(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<VocabduelRound | boolean> {
        return this.game.round$(+route.params.gameId).pipe(
            catchError((err) => {
                if (err.status === 403 || err.status === 404) {
                    this.snackbar.showSnackbar('snackbar.noAccessToGame');
                    return this.router.navigate(['/dashboard']);
                } else return throwError(err);
            })
        );
    }
}
