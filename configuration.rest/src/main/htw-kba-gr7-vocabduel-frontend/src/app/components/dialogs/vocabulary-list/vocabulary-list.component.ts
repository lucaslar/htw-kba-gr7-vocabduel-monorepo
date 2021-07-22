import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { VocableList } from '../../../model/vocable-list';
import { User } from '../../../model/internal/user';
import { ComplexDialogManagementService } from '../../../services/complex-dialog-management.service';

@Component({
    selector: 'app-vocabulary-list',
    templateUrl: './vocabulary-list.component.html',
    styleUrls: ['./vocabulary-list.component.scss'],
})
export class VocabularyListComponent {
    isAuthorSelf: boolean;
    author?: User;
    list: VocableList;

    private readonly currentUser?: User;

    constructor(
        @Inject(MAT_DIALOG_DATA)
        data: {
            currentUser: User;
            list: VocableList;
        },
        private readonly ref: MatDialogRef<VocabularyListComponent>,
        private readonly dialogManagement: ComplexDialogManagementService
    ) {
        this.author = data.list.author;
        this.isAuthorSelf =
            this.author && data.currentUser?.id === this.author.id;
        this.list = data.list;
        this.currentUser = data.currentUser;
    }

    openAuthorDialog(): void {
        if (this.author) {
            this.ref.close();
            this.dialogManagement.openUserDialog(this.author, this.currentUser);
        }
    }
}
