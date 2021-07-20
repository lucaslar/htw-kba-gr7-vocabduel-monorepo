import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { NavigationService } from '../../../services/navigation.service';

@Component({
    selector: 'app-registration',
    templateUrl: './registration.component.html',
    styleUrls: ['./registration.component.scss'],
})
export class RegistrationComponent {
    readonly user = {
        username: '',
        email: '',
        firstName: '',
        lastName: '',
        password: '',
        confirm: '',
    };

    constructor(
        readonly auth: AuthService,
        readonly navigation: NavigationService
    ) {}
    // TODO Error handling
}
