import { Component } from '@angular/core';
import { VocabularyService } from '../../../services/vocabulary.service';
import { VocableList } from '../../../model/vocable-list';
import { Observable } from 'rxjs';
import { AuthService } from '../../../services/auth.service';
import { User } from '../../../model/internal/user';
import { ComplexDialogManagementService } from '../../../services/complex-dialog-management.service';
import { ScoreRecord } from '../../../model/score-record';
import { ScoreService } from '../../../services/score.service';
import { PersonalFinishedGame } from '../../../model/personal-finished-game';
import { MatDialog } from '@angular/material/dialog';
import { PersonalFinishedGameComponent } from '../../dialogs/personal-finished-game/personal-finished-game.component';

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent {
    ownImports$!: Observable<VocableList[]>;
    record$!: Observable<ScoreRecord>;
    finishedGames$!: Observable<PersonalFinishedGame[]>;

    currentUser!: User;

    constructor(
        readonly vocabulary: VocabularyService,
        private readonly dialogManagement: ComplexDialogManagementService,
        private readonly dialog: MatDialog,
        auth: AuthService,
        score: ScoreService
    ) {
        auth.currentUser$.subscribe((user) => {
            if (user) {
                this.currentUser = user!;
                this.ownImports$ = vocabulary.listsOfAuthor$(user!);
                this.record$ = score.ownRecord$;
                this.finishedGames$ = score.finishedGames$;
            }
        });
    }

    onVocabularyListSelected(list: VocableList): void {
        this.dialogManagement.openVocableListDialog(list, this.currentUser);
    }

    openFinishedGame(finishedGame: PersonalFinishedGame): void {
        this.dialog.open(PersonalFinishedGameComponent, {
            data: { finishedGame, currentUser: this.currentUser },
        });
    }
}
