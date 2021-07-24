import { Component, Input } from '@angular/core';
import { GameService } from '../../../../services/game.service';
import { Observable } from 'rxjs';
import { RunningGame } from '../../../../model/running-game';
import { Router } from '@angular/router';
import { NavigationService } from '../../../../services/navigation.service';
import { ComplexDialogManagementService } from '../../../../services/complex-dialog-management.service';
import { User } from '../../../../model/internal/user';

@Component({
    selector: 'app-games',
    templateUrl: './games.component.html',
    styleUrls: ['./games.component.scss'],
})
export class GamesComponent {
    readonly openGames$: Observable<RunningGame[]>;
    @Input() currentUser!: User;

    constructor(
        readonly router: Router,
        readonly navigation: NavigationService,
        private readonly gameService: GameService,
        private readonly dialogManagement: ComplexDialogManagementService
    ) {
        this.openGames$ = gameService.openGames$;
    }

    showOpponentDetails(opponent: User): void {
        this.dialogManagement.openUserDialog(opponent, this.currentUser);
    }

    showGameDetails(): void {
        // TODO: Implement
    }
}
