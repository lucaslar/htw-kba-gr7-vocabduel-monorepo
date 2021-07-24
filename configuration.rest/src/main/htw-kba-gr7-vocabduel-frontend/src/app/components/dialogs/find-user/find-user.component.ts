import { AfterViewInit, Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NavigationService } from '../../../services/navigation.service';
import { UserService } from '../../../services/user.service';
import { User } from '../../../model/internal/user';

@Component({
    selector: 'app-find-user',
    templateUrl: './find-user.component.html',
    styleUrls: ['./find-user.component.scss'],
})
export class FindUserComponent implements AfterViewInit {
    disableAnimation = true;
    identifier: 'id' | 'username' | 'email' = 'id';
    invalidFormat = false;
    notFound = false;
    userIsSelf = false;

    constructor(
        readonly ref: MatDialogRef<FindUserComponent>,
        readonly navigation: NavigationService,
        private readonly user: UserService,
        @Inject(MAT_DIALOG_DATA) readonly currentUser: User
    ) {}

    ngAfterViewInit(): void {
        setTimeout(() => (this.disableAnimation = false));
    }

    searchByIdentifier(value: string): void {
        this.user.getUser$({ [this.identifier]: value } as any).subscribe(
            (res) => {
                if (res.id === this.currentUser.id) this.userIsSelf = true;
                else this.ref.close(res);
            },
            (err) => {
                if (err.status === 400) this.invalidFormat = true;
                else if (err.status === 404) this.notFound = true;
                else throw err;
            }
        );
    }
}
