import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { StorageService } from './storage.service';
import { tap } from 'rxjs/operators';
import { LanguageSet } from '../model/language-set';

@Injectable({
    providedIn: 'root',
})
export class VocabularyService {
    constructor(
        private readonly http: HttpClient,
        private readonly storage: StorageService
    ) {}

    get supportedLanguages$(): Observable<string[]> {
        const url = `${this.storage.endpointUrl}/vocabulary/supported-languages`;
        return this.http.get<string[]>(url).pipe(tap((langs) => langs.sort()));
    }

    referencesFor$(lang: string): Observable<string[]> {
        const url = `${this.storage.endpointUrl}/vocabulary/language-references/${lang}`;
        return this.http.get<string[]>(url).pipe(tap((langs) => langs.sort()));
    }

    get languageSets$(): Observable<LanguageSet[]> {
        const url = `${this.storage.endpointUrl}/vocabulary/language-sets`;
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

    importGnuFile$(data: string): Observable<void> {
        const url = `${this.storage.endpointUrl}/vocabulary/import-gnu`;
        return this.http.post<void>(url, data);
    }

    private static sortByTitle(
        a: { title: string },
        b: { title: string }
    ): number {
        return a.title.toLowerCase() < b.title.toLowerCase() ? -1 : 1;
    }
}
