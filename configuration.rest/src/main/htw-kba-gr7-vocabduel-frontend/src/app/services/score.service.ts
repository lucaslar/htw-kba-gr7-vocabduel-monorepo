import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { PersonalFinishedGame } from '../model/personal-finished-game';

@Injectable({
    providedIn: 'root',
})
export class ScoreService {
    constructor(private readonly http: HttpClient) {}

    finishGame$(gameId: number): Observable<PersonalFinishedGame> {
        const url = `${environment.endpointUrl}/score/finish-game`;
        return this.http.post<PersonalFinishedGame>(url, '' + gameId);
    }
}
