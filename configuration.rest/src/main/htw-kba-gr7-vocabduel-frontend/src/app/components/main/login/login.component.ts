import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { LoginData } from '../../../model/internal/login-data';
import { NavigationService } from '../../../services/navigation.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
    data: LoginData = { email: '', password: '' };
    isInvalid = false;

    constructor(
        readonly navigation: NavigationService,
        private readonly auth: AuthService
    ) {}

    login(): void {
        this.data.email = this.data.email.trim();
        this.auth.login$(this.data).subscribe(
            () => {},
            (err) => {
                [this.data.email, this.data.password] = ['', ''];
                if (err.status === 400) this.isInvalid = true;
                else throw err;
            }
        );
    }
}
