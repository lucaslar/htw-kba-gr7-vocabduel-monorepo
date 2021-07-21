import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { VocableList } from '../../../model/vocable-list';
import { User } from '../../../model/internal/user';

@Component({
    selector: 'app-vocabulary-list',
    templateUrl: './vocabulary-list.component.html',
    styleUrls: ['./vocabulary-list.component.scss'],
})
export class VocabularyListComponent {
    isAuthorSelf: boolean;
    author?: User;
    list: VocableList;

    constructor(
        @Inject(MAT_DIALOG_DATA)
        data: {
            self: User;
            list: VocableList;
        }
    ) {
        this.author = data.list.author;
        this.isAuthorSelf = this.author && data.self?.id === this.author.id;
        this.list = data.list;
    }
}
