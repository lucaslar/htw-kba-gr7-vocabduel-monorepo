import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { User } from '../model/internal/user';
import { UserDetailsComponent } from '../components/dialogs/user-details/user-details.component';
import { VocabularyService } from './vocabulary.service';
import { VocableList } from '../model/vocable-list';
import { VocabularyListComponent } from '../components/dialogs/vocabulary-list/vocabulary-list.component';

@Injectable({
    providedIn: 'root',
})
export class ComplexDialogManagementService {
    constructor(
        private readonly dialog: MatDialog,
        private readonly vocabulary: VocabularyService
    ) {}

    openUserDialog(user: User, currentUser?: User | null): void {
        this.vocabulary.listsOfAuthor$(user).subscribe((vocableLists) => {
            // TODO: Add score stats
            this.dialog.open(UserDetailsComponent, {
                data: { currentUser, user, vocableLists },
            });
        });
    }

    openVocableListDialog(list: VocableList, currentUser?: User | null): void {
        this.dialog.open(VocabularyListComponent, {
            data: { currentUser, list },
        });
    }
}
