import { Component } from '@angular/core';
import { VocabularyService } from '../../../services/vocabulary.service';
import { VocableList } from '../../../model/vocable-list';
import { Observable } from 'rxjs';
import { AuthService } from '../../../services/auth.service';
import { User } from '../../../model/internal/user';
import { ComplexDialogManagementService } from '../../../services/complex-dialog-management.service';

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent {
    ownImports$!: Observable<VocableList[]>;

    currentUser!: User;

    constructor(
        readonly vocabulary: VocabularyService,
        private readonly auth: AuthService,
        private readonly dialogManagement: ComplexDialogManagementService
    ) {
        this.auth.currentUser$.subscribe((user) => {
            if (user) {
                this.currentUser = user!;
                this.ownImports$ = vocabulary.listsOfAuthor$(user!);
            }
        });
    }

    onVocabularyListSelected(list: VocableList): void {
        this.dialogManagement.openVocableListDialog(list, this.currentUser);
    }
}
