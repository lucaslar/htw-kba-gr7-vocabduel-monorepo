import { Component, Input, OnInit } from '@angular/core';
import { TranslationGroup } from '../../../../model/translation-group';

@Component({
    selector: 'app-translation-group',
    templateUrl: './translation-group.component.html',
    styleUrls: ['./translation-group.component.scss'],
})
export class TranslationGroupComponent implements OnInit {
    synonymList!: string;

    @Input() translationGroup!: TranslationGroup;

    ngOnInit(): void {
        this.synonymList = this.translationGroup.synonyms.join(', ');
    }
}
