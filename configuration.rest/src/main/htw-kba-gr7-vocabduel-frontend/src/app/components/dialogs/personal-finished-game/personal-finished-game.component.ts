import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PersonalFinishedGame } from '../../../model/personal-finished-game';

@Component({
    selector: 'app-personal-finished-game',
    templateUrl: './personal-finished-game.component.html',
    styleUrls: ['./personal-finished-game.component.scss'],
})
export class PersonalFinishedGameComponent {
    constructor(
        @Inject(MAT_DIALOG_DATA) readonly finishedGame: PersonalFinishedGame
    ) {}
}
