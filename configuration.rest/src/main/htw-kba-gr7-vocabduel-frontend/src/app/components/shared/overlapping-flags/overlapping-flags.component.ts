import { Component, Input } from '@angular/core';
import { LanguageSet } from '../../../model/language-set';

@Component({
    selector: 'app-overlapping-flags',
    templateUrl: './overlapping-flags.component.html',
    styleUrls: ['./overlapping-flags.component.scss'],
})
export class OverlappingFlagsComponent {
    @Input() languageFrom!: string;
    @Input() languageTo!: string;
}
