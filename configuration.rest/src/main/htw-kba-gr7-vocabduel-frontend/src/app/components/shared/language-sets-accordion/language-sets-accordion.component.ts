import { Component, EventEmitter, Input, Output } from '@angular/core';
import { LanguageSet } from '../../../model/language-set';
import { VocableList } from '../../../model/vocable-list';
import { User } from '../../../model/internal/user';

@Component({
    selector: 'app-language-sets-accordion',
    templateUrl: './language-sets-accordion.component.html',
    styleUrls: ['./language-sets-accordion.component.scss'],
})
export class LanguageSetsAccordionComponent {
    @Input() languageSets!: LanguageSet[];
    @Input() showDelete = true;
    @Input() currentUser?: User;
    @Output() deleteClicked: EventEmitter<VocableList> = new EventEmitter();
    @Output() listSelected: EventEmitter<VocableList> = new EventEmitter();

    onDeleteClicked(list: VocableList, event: MouseEvent): void {
        event.stopPropagation();
        this.deleteClicked.emit(list);
    }
}
