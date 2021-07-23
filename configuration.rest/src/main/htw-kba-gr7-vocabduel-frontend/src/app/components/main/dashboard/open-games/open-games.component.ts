import { Component } from '@angular/core';
import { GameService } from '../../../../services/game.service';
import { Observable } from 'rxjs';
import { RunningGame } from '../../../../model/running-game';

@Component({
    selector: 'app-open-games',
    templateUrl: './open-games.component.html',
    styleUrls: ['./open-games.component.scss'],
})
export class OpenGamesComponent {
    readonly openGames$: Observable<RunningGame[]>;

    constructor(private readonly gameService: GameService) {
        this.openGames$ = gameService.openGames$;
    }

    showOpponentDetails(): void {
        // TODO: Implement
    }

    showGameDetails(): void {
        // TODO: Implement
    }

    play(): void {
        // TODO: Implement
    }
}
