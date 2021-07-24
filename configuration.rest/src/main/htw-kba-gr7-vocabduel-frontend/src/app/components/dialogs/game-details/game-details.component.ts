import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { RunningGame } from '../../../model/running-game';
import { ComplexDialogManagementService } from '../../../services/complex-dialog-management.service';
import { User } from '../../../model/internal/user';
import { NavigationService } from '../../../services/navigation.service';
import { VocableList } from '../../../model/vocable-list';
import { VocabularyService } from '../../../services/vocabulary.service';

@Component({
    selector: 'app-game-details',
    templateUrl: './game-details.component.html',
    styleUrls: ['./game-details.component.scss'],
})
export class GameDetailsComponent {
    readonly game: RunningGame;
    readonly currentUser: User;

    constructor(
        @Inject(MAT_DIALOG_DATA) data: { game: RunningGame; currentUser: User },
        readonly navigation: NavigationService,
        private readonly ref: MatDialogRef<GameDetailsComponent>,
        private readonly dialogManagement: ComplexDialogManagementService,
        private readonly vocabulary: VocabularyService
    ) {
        this.currentUser = data.currentUser;
        this.game = data.game;
    }

    openOpponentDialog(): void {
        this.ref.close();
        this.dialogManagement.openUserDialog(
            this.game.opponent,
            this.currentUser
        );
    }

    onListSelected(list: VocableList): void {
        this.vocabulary.listById$(list.id).subscribe((loaded) => {
            this.ref.close();
            this.dialogManagement.openVocableListDialog(
                loaded,
                this.currentUser
            );
        });
    }
}
