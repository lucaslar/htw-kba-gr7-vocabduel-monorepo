import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { tap } from 'rxjs/operators';
import { LanguageSet } from '../model/language-set';
import { VocableList } from '../model/vocable-list';
import { User } from '../model/internal/user';

@Injectable({
    providedIn: 'root',
})
export class VocabularyService {
    constructor(private readonly http: HttpClient) {}

    private static sortByTitle(
        a: { title: string },
        b: { title: string }
    ): number {
        return a.title.toLowerCase() < b.title.toLowerCase() ? -1 : 1;
    }

    get supportedLanguages$(): Observable<string[]> {
        const url = `${environment.endpointUrl}/vocabulary/supported-languages`;
        return this.http.get<string[]>(url).pipe(tap((langs) => langs.sort()));
    }

    referencesFor$(lang: string): Observable<string[]> {
        const url = `${environment.endpointUrl}/vocabulary/language-references/${lang}`;
        return this.http.get<string[]>(url).pipe(tap((langs) => langs.sort()));
    }

    get languageSets$(): Observable<LanguageSet[]> {
        const url = `${environment.endpointUrl}/vocabulary/language-sets`;
        return this.http.get<LanguageSet[]>(url).pipe(
            tap((sets) => {
                sets.forEach((ls) => {
                    ls.vocableUnits.sort(VocabularyService.sortByTitle);
                    ls.vocableUnits.forEach((u) => {
                        u.vocableLists.sort(VocabularyService.sortByTitle);
                    });
                });

                return sets.sort((a, b) => {
                    return a.knownLanguage + a.learntLanguage <
                        b.knownLanguage + b.learntLanguage
                        ? -1
                        : 1;
                });
            })
        );
    }

    importGnuFile$(data: string): Observable<VocableList> {
        const url = `${environment.endpointUrl}/vocabulary/import-gnu`;
        return this.http.post<VocableList>(url, data);
    }

    deleteVocableList$(list: VocableList): Observable<void> {
        const url = `${environment.endpointUrl}/vocabulary/delete-list/${list.id}`;
        return this.http.delete<void>(url);
    }

    listsOfAuthor$(user: User): Observable<VocableList[]> {
        const url = `${environment.endpointUrl}/vocabulary/lists-of-author/${user.id}`;
        return this.http.get<VocableList[]>(url);
    }

    listById$(id: number): Observable<VocableList> {
        const url = `${environment.endpointUrl}/vocabulary/list/${id}`;
        return this.http.get<VocableList>(url);
    }
}
