import { Component, OnInit } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { AuthService } from '../../../services/auth.service';
import { StorageService } from '../../../services/storage.service';
import { PasswordData } from '../../../model/internal/password-data';
import { SnackbarService } from '../../../services/snackbar.service';
import { UserService } from '../../../services/user.service';
import { User } from '../../../model/internal/user';
import { NavigationService } from '../../../services/navigation.service';

@Component({
    selector: 'app-settings',
    templateUrl: './settings.component.html',
    styleUrls: ['./settings.component.scss'],
})
export class SettingsComponent implements OnInit {
    readonly defaultEndpoint = environment.endpointUrl;

    readonly passwordData: PasswordData = {
        currentPassword: '',
        newPassword: '',
        confirm: '',
    };

    userData!: User;
    currentUser?: User;

    constructor(
        readonly auth: AuthService,
        readonly user: UserService,
        readonly navigation: NavigationService,
        readonly storage: StorageService,
        readonly snackbar: SnackbarService
    ) {}

    ngOnInit(): void {
        this.auth.currentUser$.subscribe((user) => {
            if (user) {
                const { authTokens, ...userData } = user;
                this.bothUserData = userData;
            }
        });
    }

    updateUserData(): void {
        this.user.updateAccount$(this.userData).subscribe((user) => {
            this.bothUserData = user;
            console.log(user);
            this.snackbar.showSnackbar('snackbar.userUpdated', user);
        });
    }

    set bothUserData(user: User) {
        this.userData = Object.assign({}, user);
        this.currentUser = Object.assign({}, user);
    }
}
