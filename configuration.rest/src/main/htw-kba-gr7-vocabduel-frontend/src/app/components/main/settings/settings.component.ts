import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { StorageService } from '../../../services/storage.service';
import { SnackbarService } from '../../../services/snackbar.service';
import { UserService } from '../../../services/user.service';
import { User } from '../../../model/internal/user';
import { NavigationService } from '../../../services/navigation.service';
import { GameService } from '../../../services/game.service';
import {
    FormBuilder,
    FormControl,
    FormGroup,
    Validators,
} from '@angular/forms';
import { nameValidation, pwdVal } from '../../../model/vocabduel-validation';
import { MatDialog } from '@angular/material/dialog';
import { ManageableErrorComponent } from '../../dialogs/manageable-error/manageable-error.component';

@Component({
    selector: 'app-settings',
    templateUrl: './settings.component.html',
    styleUrls: ['./settings.component.scss'],
})
export class SettingsComponent implements OnInit {
    userForm!: FormGroup;
    usernameCtrl!: FormControl;
    emailCtrl!: FormControl;
    firstNameCtrl!: FormControl;
    lastNameCtrl!: FormControl;

    pwdForm!: FormGroup;
    currentPasswordCtrl!: FormControl;
    passwordCtrl!: FormControl;
    confirmPasswordCtrl!: FormControl;

    currentUser?: User;

    constructor(
        readonly auth: AuthService,
        readonly game: GameService,
        readonly navigation: NavigationService,
        readonly storage: StorageService,
        readonly snackbar: SnackbarService,
        private readonly user: UserService,
        private readonly formBuilder: FormBuilder,
        private readonly dialog: MatDialog
    ) {}

    ngOnInit(): void {
        this.usernameCtrl = new FormControl('', [Validators.required]);
        this.emailCtrl = new FormControl('', [
            Validators.required,
            Validators.pattern('^(.+)@(.+)$'),
        ]);
        this.firstNameCtrl = new FormControl('', [
            Validators.required,
            nameValidation,
        ]);
        this.lastNameCtrl = new FormControl('', [
            Validators.required,
            nameValidation,
        ]);

        this.currentPasswordCtrl = new FormControl('', [Validators.required]);
        this.passwordCtrl = new FormControl('', [
            Validators.required,
            Validators.minLength(8),
            pwdVal,
        ]);
        this.confirmPasswordCtrl = new FormControl('', [Validators.required]);

        this.userForm = this.formBuilder.group({
            username: this.usernameCtrl,
            email: this.emailCtrl,
            firstName: this.firstNameCtrl,
            lastName: this.lastNameCtrl,
        });

        this.pwdForm = this.formBuilder.group({
            currentPassword: this.currentPasswordCtrl,
            password: this.passwordCtrl,
            confirm: this.confirmPasswordCtrl,
        });

        const checkPwds = () => {
            if (
                this.passwordCtrl.value &&
                this.passwordCtrl.value !== this.confirmPasswordCtrl.value
            ) {
                this.confirmPasswordCtrl.setErrors({ pwdMustMatch: true });
            }
        };

        this.passwordCtrl.valueChanges.subscribe(() => checkPwds());
        this.confirmPasswordCtrl.valueChanges.subscribe(() => checkPwds());

        this.auth.currentUser$.subscribe((user) => {
            if (user) {
                const { authTokens, ...userData } = user;
                this.currentUser = userData;
                this.updateFormControls(this.currentUser);
            }
        });
    }

    updateUserData(): void {
        this.user
            .updateAccount$({
                id: this.currentUser!.id,
                firstName: this.firstNameCtrl.value.trim(),
                lastName: this.lastNameCtrl.value.trim(),
                username: this.usernameCtrl.value.trim(),
                email: this.emailCtrl.value.trim(),
            })
            .subscribe(
                (user) => {
                    this.currentUser = user;
                    this.updateFormControls(user);
                    this.snackbar.showSnackbar('snackbar.userUpdated', user);
                },
                (err) => {
                    if (err.status === 400) {
                        this.dialog.open(ManageableErrorComponent, {
                            data: err.error,
                        });
                    } else throw err;
                }
            );
    }

    updatePassword(): void {
        this.auth
            .updatePassword$({
                currentPassword: this.currentPasswordCtrl.value,
                newPassword: this.passwordCtrl.value,
                confirm: this.confirmPasswordCtrl.value,
            })
            .subscribe(
                () => {
                    this.currentPasswordCtrl.setValue('');
                    this.passwordCtrl.setValue('');
                    this.confirmPasswordCtrl.setValue('');
                    this.snackbar.showSnackbar('snackbar.passwordUpdated');
                },
                (err) => {
                    if (err.status === 400) {
                        this.dialog.open(ManageableErrorComponent, {
                            data: err.error,
                        });
                    } else throw err;
                }
            );
    }

    updateFormControls(user: User): void {
        this.usernameCtrl.setValue(user.username);
        this.emailCtrl.setValue(user.email);
        this.firstNameCtrl.setValue(user.firstName);
        this.lastNameCtrl.setValue(user.lastName);
    }
}
