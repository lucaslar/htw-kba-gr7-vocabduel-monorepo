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

@Injectable({
    providedIn: 'root',
})
export class GameAccessResolver implements Resolve<VocabduelRound | boolean> {
    constructor(private game: GameService, private router: Router) {}

    resolve(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<VocabduelRound | boolean> {
        return this.game.round$(+route.params.gameId).pipe(
            catchError((err) => {
                if (err.status === 403 || err.status === 404) {
                    return this.router.navigate(['/dashboard']);
                } else return throwError(err);
            })
        );
    }
}
