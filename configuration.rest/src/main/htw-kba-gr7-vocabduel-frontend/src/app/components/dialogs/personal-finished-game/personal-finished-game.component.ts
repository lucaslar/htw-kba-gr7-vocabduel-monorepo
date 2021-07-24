import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { PersonalFinishedGame } from '../../../model/personal-finished-game';
import { ComplexDialogManagementService } from '../../../services/complex-dialog-management.service';
import { User } from '../../../model/internal/user';

@Component({
    selector: 'app-personal-finished-game',
    templateUrl: './personal-finished-game.component.html',
    styleUrls: ['./personal-finished-game.component.scss'],
})
export class PersonalFinishedGameComponent {
    readonly finishedGame: PersonalFinishedGame;
    private readonly currentUser: User;

    constructor(
        @Inject(MAT_DIALOG_DATA)
        data: { finishedGame: PersonalFinishedGame; currentUser: User },
        private readonly ref: MatDialogRef<PersonalFinishedGameComponent>,
        private readonly dialogManagement: ComplexDialogManagementService
    ) {
        this.finishedGame = data.finishedGame;
        this.currentUser = data.currentUser;
    }

    openOpponentDialog(): void {
        if (this.finishedGame.opponent) {
            this.ref.close();
            this.dialogManagement.openUserDialog(
                this.finishedGame.opponent,
                this.currentUser
            );
        }
    }
}
