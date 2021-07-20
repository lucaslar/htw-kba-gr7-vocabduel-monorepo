import { Injectable } from '@angular/core';
import {
    HttpErrorResponse,
    HttpEvent,
    HttpHandler,
    HttpInterceptor,
    HttpRequest,
} from '@angular/common/http';
import { StorageService } from '../services/storage.service';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { catchError, filter, finalize, switchMap, take } from 'rxjs/operators';
import { NavigationService } from '../services/navigation.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    private refreshingInProgress = false;
    private readonly accessTokenSubject: BehaviorSubject<string | null> =
        new BehaviorSubject<string | null>(null);

    constructor(
        private readonly storage: StorageService,
        private readonly auth: AuthService,
        private readonly navigation: NavigationService
    ) {}

    intercept(
        req: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        const token = this.storage.token;
        this.navigation.isLoadingIndicated = true;
        return next.handle(this.appendAuthHeader(req, token)).pipe(
            finalize(() => (this.navigation.isLoadingIndicated = false)),
            catchError((err) => {
                if (err instanceof HttpErrorResponse && err.status === 401) {
                    return this.storage.refreshToken && token
                        ? this.refreshToken$(req, next)
                        : this.logoutAndThrow(err);
                } else return throwError(err);
            })
        );
    }

    private appendAuthHeader(
        req: HttpRequest<any>,
        token: string | null
    ): HttpRequest<any> {
        return token
            ? req.clone({
                  headers: req.headers.set('Authorization', 'Bearer ' + token),
              })
            : req;
    }

    private logoutAndThrow(err: any): Observable<HttpEvent<any>> {
        this.auth.logout();
        return throwError(err);
    }

    private refreshToken$(
        request: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        if (!this.refreshingInProgress) {
            this.refreshingInProgress = true;
            this.accessTokenSubject.next(null);

            return this.auth.refreshToken$.pipe(
                switchMap((res) => {
                    this.refreshingInProgress = false;
                    this.accessTokenSubject.next(res.token);
                    return next.handle(
                        this.appendAuthHeader(request, res.token)
                    );
                }),
                catchError((err) => {
                    return err instanceof HttpErrorResponse &&
                        err.status === 403
                        ? this.logoutAndThrow(err)
                        : throwError(err);
                })
            );
        } else {
            return this.accessTokenSubject.pipe(
                filter((token) => token !== null),
                take(1),
                switchMap((token) => {
                    return next.handle(this.appendAuthHeader(request, token));
                })
            );
        }
    }
}
