import { Component } from '@angular/core';
import { User } from '../../../model/internal/user';
import { VocableList } from '../../../model/vocable-list';
import { NavigationService } from '../../../services/navigation.service';
import { MatDialog } from '@angular/material/dialog';
import { FindUserComponent } from '../../dialogs/find-user/find-user.component';

@Component({
    selector: 'app-start-game',
    templateUrl: './start-game.component.html',
    styleUrls: ['./start-game.component.scss'],
})
export class StartGameComponent {
    opponent?: User;
    lists?: VocableList[];

    constructor(
        readonly navigation: NavigationService,
        private readonly dialog: MatDialog
    ) {}

    get totalVocables(): number {
        const count = this.lists
            ?.map((l) => l.vocables.length)
            .reduce((c, p) => c + p, 0);
        return count ?? 0;
    }

    selectOpponent(): void {
        this.dialog
            .open(FindUserComponent, { width: '80vw' })
            .afterClosed()
            .subscribe((user) => {
                if (user) this.opponent = user;
            });
    }

    selectLists(): void {
        // TODO Implement
    }
}
