import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { VocableList } from '../../../model/vocable-list';

@Component({
    selector: 'app-confirm-delete',
    templateUrl: './confirm-delete.component.html',
    styleUrls: ['./confirm-delete.component.scss'],
})
export class ConfirmDeleteComponent {
    constructor(@Inject(MAT_DIALOG_DATA) readonly list: VocableList) {}
}
