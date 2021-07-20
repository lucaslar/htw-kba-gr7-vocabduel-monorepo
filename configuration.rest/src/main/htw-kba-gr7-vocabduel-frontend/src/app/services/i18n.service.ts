import { Injectable } from '@angular/core';
import { Language } from '../model/internal/language';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
    providedIn: 'root',
})
export class I18nService {
    readonly supportedLanguages: Language[] = [
        new Language('en', 'English'),
        // new Language('de', 'Deutsch'), // TODO Add support for de?
    ];

    constructor(private readonly translate: TranslateService) {
        this.translate.setDefaultLang('en');
    }

    initialize(): void {
        const iso = this.supportedUserLanguageId ?? this.translate.defaultLang;
        this.currentLanguage = this.supportedLanguages.find(
            (l) => l.iso === iso
        ) as Language;
    }

    set currentLanguage(language: Language) {
        this.translate.use(language.iso);
    }

    get currentLanguageIso(): string {
        return this.translate.currentLang;
    }

    private get supportedUserLanguageId(): string | undefined {
        return [...navigator.languages, navigator.language]
            .filter((l) => !!l)
            .map((l) => l.split('-')[0])
            .find((l) =>
                this.supportedLanguages
                    .map((supported) => supported.iso)
                    .includes(l)
            );
    }
}
