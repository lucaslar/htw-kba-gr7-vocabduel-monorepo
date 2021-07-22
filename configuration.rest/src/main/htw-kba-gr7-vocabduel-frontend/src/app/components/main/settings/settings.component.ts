import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { StorageService } from '../../../services/storage.service';
import { PasswordData } from '../../../model/internal/password-data';
import { SnackbarService } from '../../../services/snackbar.service';
import { UserService } from '../../../services/user.service';
import { User } from '../../../model/internal/user';
import { NavigationService } from '../../../services/navigation.service';
import { GameService } from '../../../services/game.service';

@Component({
    selector: 'app-settings',
    templateUrl: './settings.component.html',
    styleUrls: ['./settings.component.scss'],
})
export class SettingsComponent implements OnInit {
    readonly passwordData: PasswordData = {
        currentPassword: '',
        newPassword: '',
        confirm: '',
    };

    userData!: User;
    currentUser?: User;

    constructor(
        readonly auth: AuthService,
        readonly game: GameService,
        readonly navigation: NavigationService,
        readonly storage: StorageService,
        readonly snackbar: SnackbarService,
        private readonly user: UserService
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
