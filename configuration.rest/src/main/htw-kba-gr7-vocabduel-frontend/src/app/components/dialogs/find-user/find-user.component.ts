import { AfterViewInit, Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { NavigationService } from '../../../services/navigation.service';
import { UserService } from '../../../services/user.service';

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

    constructor(
        readonly ref: MatDialogRef<FindUserComponent>,
        readonly navigation: NavigationService,
        private readonly user: UserService
    ) {}

    ngAfterViewInit(): void {
        setTimeout(() => (this.disableAnimation = false));
    }

    searchByIdentifier(value: string): void {
        this.user.getUser$({ [this.identifier]: value } as any).subscribe(
            (res) => this.ref.close(res),
            (err) => {
                if (err.status === 400) this.invalidFormat = true;
                else if (err.status === 404) this.notFound = true;
                else throw err;
            }
        );
    }
}
