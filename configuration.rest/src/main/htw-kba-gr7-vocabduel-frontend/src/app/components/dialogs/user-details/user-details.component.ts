import { AfterViewInit, Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { User } from '../../../model/internal/user';
import { VocableList } from '../../../model/vocable-list';
import { ComplexDialogManagementService } from '../../../services/complex-dialog-management.service';
import { ScoreRecord } from '../../../model/score-record';

@Component({
    selector: 'app-user-details',
    templateUrl: './user-details.component.html',
    styleUrls: ['./user-details.component.scss'],
})
export class UserDetailsComponent implements AfterViewInit {
    user!: User;
    vocableLists!: VocableList[];
    currentUser?: User;
    scoreRecord?: ScoreRecord;

    disableAnimation = true;

    constructor(
        @Inject(MAT_DIALOG_DATA)
        data: {
            currentUser: User;
            user: User;
            vocableLists: VocableList[];
            scoreRecord: ScoreRecord;
        },
        private readonly ref: MatDialogRef<UserDetailsComponent>,
        private readonly dialogManagement: ComplexDialogManagementService
    ) {
        this.currentUser = data.currentUser;
        this.user = data.user;
        this.vocableLists = data.vocableLists;
        this.scoreRecord = data.scoreRecord;
    }

    ngAfterViewInit(): void {
        setTimeout(() => (this.disableAnimation = false));
    }

    onListSelected(list: VocableList): void {
        this.ref.close();
        this.dialogManagement.openVocableListDialog(list, this.currentUser);
    }
}
