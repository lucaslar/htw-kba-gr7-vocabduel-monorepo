import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
    selector: 'app-language-references',
    templateUrl: './language-references.component.html',
    styleUrls: ['./language-references.component.scss'],
})
export class LanguageReferencesComponent {
    constructor(
        @Inject(MAT_DIALOG_DATA)
        readonly data: { lang: string; references: string[] }
    ) {}
}
