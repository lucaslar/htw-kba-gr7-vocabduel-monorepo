import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GameService } from '../../../services/game.service';
import { VocabduelRound } from '../../../model/vocabduel-round';
import { Observable } from 'rxjs';
import { CorrectAnswerResult } from '../../../model/correct-answer-result';
import { NavigationService } from '../../../services/navigation.service';
import { map, tap } from 'rxjs/operators';
import { TranslationGroup } from '../../../model/translation-group';
import { SnackbarService } from '../../../services/snackbar.service';
import { ScoreService } from '../../../services/score.service';
import { MatDialog } from '@angular/material/dialog';
import { PersonalFinishedGameComponent } from '../../dialogs/personal-finished-game/personal-finished-game.component';
import { AuthService } from '../../../services/auth.service';
import { User } from '../../../model/internal/user';

@Component({
    selector: 'app-play-game',
    templateUrl: './play-game.component.html',
    styleUrls: ['./play-game.component.scss'],
})
export class PlayGameComponent {
    gameId: number;
    round$!: Observable<VocabduelRound>;
    result?: CorrectAnswerResult;
    selected?: number;
    correctIndex?: number;
    answerOpts?: TranslationGroup[];
    private currentUser!: User;

    constructor(
        readonly navigation: NavigationService,
        private readonly router: Router,
        private readonly route: ActivatedRoute,
        private readonly game: GameService,
        private readonly score: ScoreService,
        private readonly snackbar: SnackbarService,
        private readonly dialog: MatDialog,
        private readonly auth: AuthService
    ) {
        this.gameId = +this.route.snapshot.params.gameId;
        this.round$ = this.route.data.pipe(
            map((d) => d.data),
            tap((round) => (this.answerOpts = round.answers))
        );

        this.auth.currentUser$.subscribe((user) => {
            if (user) this.currentUser = user;
        });
    }

    handleNext(roundNr: number): void {
        roundNr < 9
            ? this.loadQuestion()
            : this.score.finishGame$(this.gameId).subscribe(
                  (finishedGame) => {
                      this.dialog
                          .open(PersonalFinishedGameComponent, {
                              data: {
                                  finishedGame,
                                  currentUser: this.currentUser,
                              },
                          })
                          .afterClosed()
                          .subscribe(() => {
                              this.router.navigate(['dashboard']).then();
                          });
                  },
                  (err) => {
                      if (err?.status === 400) {
                          this.router.navigate(['dashboard']).then();
                      } else throw err;
                  }
              );
    }

    answerQuestion(roundNr: number, index: number): void {
        if (
            this.selected === undefined &&
            !this.navigation.isLoadingIndicated
        ) {
            this.selected = index;
            this.game.answer$(this.gameId, roundNr, index).subscribe(
                (result) => {
                    this.result = result;

                    if (result.result === 'WIN') {
                        this.snackbar.showSnackbar('snackbar.answer.right');
                    } else {
                        this.snackbar.showSnackbar('snackbar.answer.wrong');
                        this.correctIndex = this.answerOpts?.findIndex((opt) =>
                            opt.synonyms.every((s) =>
                                result.correctAnswer.synonyms.includes(s)
                            )
                        );
                    }
                },
                (err) => {
                    if (err.status === 403 || err.status === 400) {
                        this.router
                            .navigate(['dashboard'])
                            .then(() =>
                                this.snackbar.showSnackbar(
                                    'snackbar.questionAlreadyAnswered'
                                )
                            );
                    }
                }
            );
        }
    }

    private loadQuestion(): void {
        delete this.result;
        delete this.selected;
        delete this.correctIndex;
        delete this.answerOpts;

        this.round$ = this.game
            .round$(this.gameId)
            .pipe(tap((round) => (this.answerOpts = round.answers)));
    }
}
