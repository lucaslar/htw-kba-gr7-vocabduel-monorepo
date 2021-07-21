import { AfterViewInit, Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { User } from '../../../model/internal/user';
import { VocableList } from '../../../model/vocable-list';

@Component({
    selector: 'app-user-details',
    templateUrl: './user-details.component.html',
    styleUrls: ['./user-details.component.scss'],
})
export class UserDetailsComponent implements AfterViewInit {
    user!: User;
    vocableLists!: VocableList[];
    currentUser?: User;

    disableAnimation = true;

    constructor(
        @Inject(MAT_DIALOG_DATA)
        data: {
            currentUser: User;
            user: User;
            vocableLists: VocableList[];
        }
    ) {
        this.currentUser = data.currentUser;
        this.user = data.user;
        this.vocableLists = data.vocableLists;
    }

    ngAfterViewInit(): void {
        setTimeout(() => (this.disableAnimation = false));
    }
}
