import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { User } from '../../../model/internal/user';
import { VocableList } from '../../../model/vocable-list';

@Component({
    selector: 'app-user-details',
    templateUrl: './user-details.component.html',
    styleUrls: ['./user-details.component.scss'],
})
export class UserDetailsComponent {
    user!: User;
    vocableLists!: VocableList[];
    ownId?: number;

    constructor(
        @Inject(MAT_DIALOG_DATA)
        data: {
            ownId: number;
            user: User;
            vocableLists: VocableList[];
        }
    ) {
        this.ownId = data.ownId;
        this.user = data.user;
        this.vocableLists = data.vocableLists;
    }
}
