import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { User } from '../model/internal/user';
import { UserDetailsComponent } from '../components/dialogs/user-details/user-details.component';
import { VocabularyService } from './vocabulary.service';
import { VocableList } from '../model/vocable-list';
import { VocabularyListComponent } from '../components/dialogs/vocabulary-list/vocabulary-list.component';
import { ScoreRecord } from '../model/score-record';
import { ScoreService } from './score.service';

@Injectable({
    providedIn: 'root',
})
export class ComplexDialogManagementService {
    constructor(
        private readonly dialog: MatDialog,
        private readonly score: ScoreService,
        private readonly vocabulary: VocabularyService
    ) {}

    openUserDialog(user: User, currentUser?: User | null): void {
        const openDialog = (
            vocableLists: VocableList[],
            scoreRecord?: ScoreRecord
        ) => {
            this.dialog.open(UserDetailsComponent, {
                data: { currentUser, user, vocableLists, scoreRecord },
            });
        };

        this.vocabulary.listsOfAuthor$(user).subscribe((vocableLists) => {
            currentUser
                ? this.score
                      .userRecord$(user)
                      .subscribe((record) => openDialog(vocableLists, record))
                : openDialog(vocableLists);
        });
    }

    openVocableListDialog(list: VocableList, currentUser?: User | null): void {
        this.dialog.open(VocabularyListComponent, {
            data: { currentUser, list },
        });
    }
}
