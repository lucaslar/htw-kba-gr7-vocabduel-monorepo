import { Injectable } from '@angular/core';
import { User } from '../model/internal/user';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { SnackbarService } from './snackbar.service';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';
import { RunningGame } from '../model/running-game';
import { tap } from 'rxjs/operators';
import { VocabduelRound } from '../model/vocabduel-round';
import { CorrectAnswerResult } from '../model/correct-answer-result';
import { VocableList } from '../model/vocable-list';

@Injectable({
    providedIn: 'root',
})
export class GameService {
    constructor(
        private readonly http: HttpClient,
        private readonly auth: AuthService,
        private readonly snackbar: SnackbarService
    ) {}

    get openGames$(): Observable<RunningGame[]> {
        const url = `${environment.endpointUrl}/game/open-games`;
        return this.http.get<RunningGame[]>(url).pipe(
            tap((r) => {
                return r.sort((a, b) => {
                    return a.finishedRoundsSelf - b.finishedRoundsSelf;
                });
            })
        );
    }

    round$(gameId: number): Observable<VocabduelRound> {
        const url = `${environment.endpointUrl}/game/current-round/${gameId}`;
        return this.http.get<VocabduelRound>(url);
    }

    answer$(
        gameId: number,
        roundNr: number,
        index: number
    ): Observable<CorrectAnswerResult> {
        const url = `${environment.endpointUrl}/game/answer/${gameId}/${roundNr}`;
        return this.http.post<CorrectAnswerResult>(url, '' + index);
    }

    deleteAccountAndWidows(user: User): void {
        const url = `${environment.endpointUrl}/game/delete-account-and-game-widows`;
        this.http.delete(url).subscribe(() => {
            this.auth.logout();
            this.snackbar.showSnackbar('snackbar.accountDeleted', user);
        });
    }

    startGame$(opponent: User, lists: VocableList[]): Observable<any> {
        const url = `${environment.endpointUrl}/game/start`;
        return this.http.post(url, {
            opponentId: opponent.id,
            vocableListIds: lists.map((l) => l.id),
        });
    }
}
