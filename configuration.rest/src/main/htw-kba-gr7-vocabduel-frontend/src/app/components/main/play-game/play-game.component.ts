import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GameService } from '../../../services/game.service';
import { VocabduelRound } from '../../../model/vocabduel-round';
import { Observable } from 'rxjs';
import { CorrectAnswerResult } from '../../../model/correct-answer-result';

@Component({
    selector: 'app-play-game',
    templateUrl: './play-game.component.html',
    styleUrls: ['./play-game.component.scss'],
})
export class PlayGameComponent implements OnInit {
    gameId: number;
    round$!: Observable<VocabduelRound>;

    constructor(
        private readonly router: Router,
        private readonly route: ActivatedRoute,
        private readonly game: GameService
    ) {
        this.gameId = +this.route.snapshot.params.gameId;
    }

    ngOnInit(): void {
        this.loadQuestion();
    }

    loadQuestion(): void {
        this.round$ = this.game.round$(this.gameId);
    }

    answerQuestion(roundNr: number, index: number): void {
        this.game.answer$(this.gameId, roundNr, index).subscribe((x) => {
            console.log(x); // TODO handle/display result
            // TODO: only execute on clicking "Next":
            this.loadQuestion();
        });
    }
}
