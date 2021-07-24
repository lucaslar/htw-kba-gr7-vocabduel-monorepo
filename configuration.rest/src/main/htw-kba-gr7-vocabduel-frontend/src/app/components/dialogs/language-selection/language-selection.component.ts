import { Component } from '@angular/core';
import { I18nService } from '../../../services/i18n.service';

@Component({
    selector: 'app-language-selection',
    templateUrl: './language-selection.component.html',
    styleUrls: ['./language-selection.component.scss'],
})
export class LanguageSelectionComponent {
    constructor(readonly i18n: I18nService) {}
}
