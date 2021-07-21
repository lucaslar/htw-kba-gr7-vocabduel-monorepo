import { LoginData } from './login-data';

export class RegistrationData extends LoginData {
    username!: string;
    firstName!: string;
    lastName!: string;
    confirm!: string;
}
