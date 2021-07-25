import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { NavigationService } from '../../../services/navigation.service';
import {
    FormBuilder,
    FormControl,
    FormGroup,
    Validators,
} from '@angular/forms';
import { pwdVal, nameValidation } from '../../../model/vocabduel-validation';

@Component({
    selector: 'app-registration',
    templateUrl: './registration.component.html',
    styleUrls: ['./registration.component.scss'],
})
export class RegistrationComponent implements OnInit {
    form!: FormGroup;
    usernameCtrl!: FormControl;
    emailCtrl!: FormControl;
    firstNameCtrl!: FormControl;
    lastNameCtrl!: FormControl;
    passwordCtrl!: FormControl;
    confirmPasswordCtrl!: FormControl;

    constructor(
        readonly navigation: NavigationService,
        private readonly auth: AuthService,
        private readonly formBuilder: FormBuilder
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
        this.passwordCtrl = new FormControl('', [
            Validators.required,
            Validators.minLength(8),
            pwdVal,
        ]);
        this.confirmPasswordCtrl = new FormControl('', [Validators.required]);

        this.form = this.formBuilder.group({
            username: this.usernameCtrl,
            email: this.emailCtrl,
            firstName: this.firstNameCtrl,
            lastName: this.lastNameCtrl,
            password: this.passwordCtrl,
            confirmPassword: this.confirmPasswordCtrl,
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
    }

    register(): void {
        this.auth.register({
            email: this.emailCtrl.value,
            firstName: this.firstNameCtrl.value,
            lastName: this.lastNameCtrl.value,
            username: this.usernameCtrl.value,
            password: this.passwordCtrl.value,
            confirm: this.confirmPasswordCtrl.value,
        });
    }
}
