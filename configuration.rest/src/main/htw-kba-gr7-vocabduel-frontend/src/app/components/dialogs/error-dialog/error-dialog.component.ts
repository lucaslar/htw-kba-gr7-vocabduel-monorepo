import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
    selector: 'app-error-dialog',
    templateUrl: './error-dialog.component.html',
    styleUrls: ['./error-dialog.component.scss'],
})
export class ErrorDialogComponent {
    readonly errorMessage: string;

    constructor(@Inject(MAT_DIALOG_DATA) error: any) {
        this.errorMessage = '';

        for (const e of [error.title, error.name, error.message, error.error]) {
            if (e && typeof e === 'string' && e.trim()) {
                this.errorMessage += e + '\n';
            }
        }

        if (!this.errorMessage.trim()) this.errorMessage = error;
    }
}
