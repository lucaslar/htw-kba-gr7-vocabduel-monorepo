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
    constructor(
        readonly auth: AuthService,
        readonly navigation: NavigationService
    ) {}
    // TODO Error handling
}
