import { Component, OnInit } from '@angular/core';
import { User } from '../../../model/internal/user';
import { VocableList } from '../../../model/vocable-list';
import { NavigationService } from '../../../services/navigation.service';
import { MatDialog } from '@angular/material/dialog';
import { FindUserComponent } from '../../dialogs/find-user/find-user.component';
import { VocabListSelectionComponent } from '../../dialogs/vocab-list-selection/vocab-list-selection.component';
import { AuthService } from '../../../services/auth.service';
import { GameService } from '../../../services/game.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-start-game',
    templateUrl: './start-game.component.html',
    styleUrls: ['./start-game.component.scss'],
})
export class StartGameComponent implements OnInit {
    opponent?: User;
    lists: VocableList[] = [];
    currentUser!: User;

    constructor(
        readonly navigation: NavigationService,
        private readonly dialog: MatDialog,
        private readonly auth: AuthService,
        private readonly game: GameService,
        private readonly router: Router
    ) {}

    ngOnInit(): void {
        this.auth.currentUser$.subscribe((user) => {
            if (user) this.currentUser = user!;
        });
    }

    get totalVocables(): number {
        const count = this.lists
            ?.map((l) => l.vocables.length)
            .reduce((c, p) => c + p, 0);
        return count ?? 0;
    }

    selectOpponent(): void {
        this.dialog
            .open(FindUserComponent, { width: '80vw', data: this.currentUser })
            .afterClosed()
            .subscribe((user) => {
                if (user) this.opponent = user;
            });
    }

    selectLists(): void {
        this.dialog
            .open(VocabListSelectionComponent)
            .afterClosed()
            .subscribe((lists) => {
                if (lists) this.lists = lists;
            });
    }

    startGame(): void {
        this.game.startGame$(this.opponent!, this.lists).subscribe((game) => {
            this.router.navigate(['play', game.id]).then();
        });
    }
}
