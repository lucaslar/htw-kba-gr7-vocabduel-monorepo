import { ErrorHandler, Injectable, NgZone } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ErrorDialogComponent } from '../components/dialogs/error-dialog/error-dialog.component';
import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { RemovedSourceComponent } from '../components/dialogs/removed-source/removed-source.component';

@Injectable({
    providedIn: 'root',
})
export class ErrorService implements ErrorHandler {
    private readonly httpErrorMap = new Map<HttpStatusCode, string>([
        [HttpStatusCode.Forbidden, 'Forbidden'],
        [HttpStatusCode.BadRequest, 'Bad Request'],
        [HttpStatusCode.NotFound, 'Not Found'],
        [HttpStatusCode.InternalServerError, 'Internal Server Error'],
    ]);

    constructor(
        private readonly dialog: MatDialog,
        private readonly ngZone: NgZone
    ) {}

    handleError(error: Error): void {
        error = this.mapStatusCode(error);
        console.error(error);
        this.ngZone.run(() => {
            if (error instanceof HttpErrorResponse && error.status === 404) {
                this.dialog.open(RemovedSourceComponent);
            } else {
                this.dialog.open(ErrorDialogComponent, {
                    data: error.stack ?? error,
                });
            }
        });
    }

    private mapStatusCode(error: Error): Error {
        if (error instanceof HttpErrorResponse) {
            const httpError = error as HttpErrorResponse;
            (httpError as any).statusText =
                this.httpErrorMap.get(httpError.status) ?? httpError.statusText;
        }
        return error;
    }
}
