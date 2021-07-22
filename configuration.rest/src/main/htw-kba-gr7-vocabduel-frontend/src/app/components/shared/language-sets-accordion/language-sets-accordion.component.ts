import { Component, EventEmitter, Input, Output } from '@angular/core';
import { LanguageSet } from '../../../model/language-set';
import { VocableList } from '../../../model/vocable-list';
import { User } from '../../../model/internal/user';
import { ConfirmDeleteComponent } from '../../dialogs/confirm-delete/confirm-delete.component';
import { MatDialog } from '@angular/material/dialog';
import { VocabularyService } from '../../../services/vocabulary.service';
import { SnackbarService } from '../../../services/snackbar.service';
import { NavigationService } from '../../../services/navigation.service';

@Component({
    selector: 'app-language-sets-accordion',
    templateUrl: './language-sets-accordion.component.html',
    styleUrls: ['./language-sets-accordion.component.scss'],
})
export class LanguageSetsAccordionComponent {
    @Input() languageSets!: LanguageSet[];
    @Input() showDelete = true;
    @Input() currentUser?: User;
    @Input() noElevation = true;
    @Output() itemDeleted: EventEmitter<VocableList> = new EventEmitter();
    @Output() listSelected: EventEmitter<VocableList> = new EventEmitter();

    constructor(
        readonly navigation: NavigationService,
        private readonly dialog: MatDialog,
        private readonly vocabulary: VocabularyService,
        private readonly snackbar: SnackbarService
    ) {}

    onDeleteClicked(list: VocableList, event: MouseEvent): void {
        event.stopPropagation();
        this.dialog
            .open(ConfirmDeleteComponent, { data: list })
            .afterClosed()
            .subscribe((res) => {
                if (res) {
                    this.vocabulary.deleteVocableList$(list).subscribe(() => {
                        this.snackbar.showSnackbar(
                            'snackbar.listDeleted',
                            list
                        );
                        this.itemDeleted.emit(list);
                    });
                }
            });
    }
}
