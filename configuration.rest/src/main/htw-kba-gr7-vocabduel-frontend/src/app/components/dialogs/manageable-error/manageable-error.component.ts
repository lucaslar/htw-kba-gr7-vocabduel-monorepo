import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
    selector: 'app-manageable-error',
    templateUrl: './manageable-error.component.html',
    styleUrls: ['./manageable-error.component.scss'],
})
export class ManageableErrorComponent {
    constructor(@Inject(MAT_DIALOG_DATA) readonly error: string) {}
}
