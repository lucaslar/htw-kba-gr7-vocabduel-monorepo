import { Injectable } from '@angular/core';
import {
    ActivatedRouteSnapshot,
    CanActivate,
    Router,
    RouterStateSnapshot,
} from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { GameService } from '../services/game.service';

@Injectable({
    providedIn: 'root',
})
export class GameAccessGuard implements CanActivate {
    constructor(private game: GameService, private router: Router) {}

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<boolean> {
        return this.game.round$(+route.params.gameId).pipe(
            map((game) => !!game),
            catchError((err) => {
                if (err.status === 403 || err.status === 404) {
                    return this.router.navigate(['/dashboard']);
                } else return throwError(err);
            })
        );
    }
}
