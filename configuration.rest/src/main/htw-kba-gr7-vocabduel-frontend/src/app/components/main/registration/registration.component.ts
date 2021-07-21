import { Component } from '@angular/core';
import { AuthService } from '../../../services/auth.service';
import { NavigationService } from '../../../services/navigation.service';
import { RegistrationData } from '../../../model/internal/registration-data';

@Component({
    selector: 'app-registration',
    templateUrl: './registration.component.html',
    styleUrls: ['./registration.component.scss'],
})
export class RegistrationComponent {
    readonly user: RegistrationData = {
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
