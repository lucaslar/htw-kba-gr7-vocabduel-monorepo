import { AfterViewInit, Component, Inject, OnInit } from '@angular/core';
import { VocabularyService } from '../../../services/vocabulary.service';
import { LanguageSet } from '../../../model/language-set';
import { Observable } from 'rxjs';
import { User } from '../../../model/internal/user';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { VocableList } from '../../../model/vocable-list';

@Component({
    selector: 'app-vocab-list-selection',
    templateUrl: './vocab-list-selection.component.html',
    styleUrls: ['./vocab-list-selection.component.scss'],
})
export class VocabListSelectionComponent implements OnInit, AfterViewInit {
    disableAnimation = true;
    languageSets$!: Observable<LanguageSet[]>;
    selection: VocableList[] = [];

    constructor(
        @Inject(MAT_DIALOG_DATA) readonly currentUser: User,
        private readonly vocabulary: VocabularyService
    ) {}

    ngOnInit(): void {
        this.languageSets$ = this.vocabulary.languageSets$;
    }

    ngAfterViewInit(): void {
        setTimeout(() => (this.disableAnimation = false));
    }

    onSelectionChanged(list: VocableList): void {
        const prevLength = this.selection.length;
        this.selection = this.selection.filter((s) => s !== list);
        if (prevLength === this.selection.length) this.selection.push(list);
    }
}
