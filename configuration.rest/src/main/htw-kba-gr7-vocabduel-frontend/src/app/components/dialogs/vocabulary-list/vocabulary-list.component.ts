import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { VocableList } from '../../../model/vocable-list';
import { User } from '../../../model/internal/user';
import { VocableUnit } from '../../../model/vocable-unit';
import { LanguageSet } from '../../../model/language-set';

@Component({
    selector: 'app-vocabulary-list',
    templateUrl: './vocabulary-list.component.html',
    styleUrls: ['./vocabulary-list.component.scss'],
})
export class VocabularyListComponent {
    isAuthorSelf: boolean;
    author?: User;
    unitTitle: string;
    languageSet: LanguageSet;
    list: VocableList;

    constructor(
        @Inject(MAT_DIALOG_DATA)
        data: {
            self: User;
            list: VocableList;
            unit: VocableUnit;
            set: LanguageSet;
        }
    ) {
        this.author = data.list.author;
        this.isAuthorSelf = this.author && data.self?.id === this.author.id;
        this.unitTitle = data.unit.title;
        this.languageSet = data.set;
        this.list = data.list;
    }
}
