import { Injectable } from '@angular/core';
import {
    CanActivate,
    ActivatedRouteSnapshot,
    RouterStateSnapshot,
    Router,
} from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { map, tap } from 'rxjs/operators';

@Injectable({
    providedIn: 'root',
})
export class UnauthorizedGuard implements CanActivate {
    constructor(private authService: AuthService, private router: Router) {}

    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<boolean> {
        return this.authService.currentUser$.pipe(
            map((user) => !user),
            tap((isLoggedOut) => {
                if (!isLoggedOut) {
                    this.router.navigate(['/dashboard']).then();
                }
            })
        );
    }
}
