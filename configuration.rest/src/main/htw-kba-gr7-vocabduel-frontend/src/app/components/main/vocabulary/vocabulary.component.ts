import { Component } from '@angular/core';
import { User } from '../../../model/internal/user';
import { AuthService } from '../../../services/auth.service';
import { VocabularyService } from '../../../services/vocabulary.service';
import { Observable } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { LanguageReferencesComponent } from '../../dialogs/language-references/language-references.component';
import { LanguageSet } from '../../../model/language-set';
import { NgxDropzoneChangeEvent } from 'ngx-dropzone';
import { SnackbarService } from '../../../services/snackbar.service';
import { NavigationService } from '../../../services/navigation.service';
import { finalize } from 'rxjs/operators';

@Component({
    selector: 'app-vocabulary',
    templateUrl: './vocabulary.component.html',
    styleUrls: ['./vocabulary.component.scss'],
})
export class VocabularyComponent {
    currentUser$?: Observable<User | null>;
    languages$?: Observable<string[]>;
    languageSets$?: Observable<LanguageSet[]>;

    file?: File;

    constructor(
        readonly navigation: NavigationService,
        private readonly vocabulary: VocabularyService,
        private readonly auth: AuthService,
        private readonly dialog: MatDialog,
        private readonly snackbar: SnackbarService
    ) {}

    ngOnInit(): void {
        this.currentUser$ = this.auth.currentUser$;
        this.languages$ = this.vocabulary.supportedLanguages$;
        this.languageSets$ = this.vocabulary.languageSets$;
    }

    onReferenceClicked(lang: string): void {
        this.vocabulary.referencesFor$(lang).subscribe((references) => {
            this.dialog.open(LanguageReferencesComponent, {
                data: { lang, references },
            });
        });
    }

    onFileDropped($event: NgxDropzoneChangeEvent): void {
        if ($event.addedFiles.length) this.file = $event.addedFiles[0];
        else this.snackbar.showSnackbar('snackbar.fileNotAccepted');
    }

    contributeFile(user: User): void {
        const fileReader = new FileReader();

        fileReader.onerror = (e) => {
            delete this.file;
            throw new Error('File could not be parsed');
        };

        fileReader.onload = () => {
            this.vocabulary
                .importGnuFile$(fileReader.result as string)
                .pipe(finalize(() => delete this.file))
                .subscribe(() => {
                    this.snackbar.showSnackbar(
                        'snackbar.thanksForImport',
                        user
                    );
                    this.languageSets$ = this.vocabulary.languageSets$;
                });
        };
        fileReader.readAsText(this.file!);
    }
}
