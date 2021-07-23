import { Component } from '@angular/core';
import { User } from '../../../model/internal/user';
import { VocableList } from '../../../model/vocable-list';
import { NavigationService } from '../../../services/navigation.service';

@Component({
    selector: 'app-start-game',
    templateUrl: './start-game.component.html',
    styleUrls: ['./start-game.component.scss'],
})
export class StartGameComponent {
    opponent?: User;
    lists?: VocableList[];

    constructor(readonly navigation: NavigationService) {}

    get totalVocables(): number {
        const count = this.lists
            ?.map((l) => l.vocables.length)
            .reduce((c, p) => c + p, 0);
        return count ?? 0;
    }

    selectOpponent(): void {
        // TODO Implement
    }

    selectLists(): void {
        // TODO Implement
    }
}
