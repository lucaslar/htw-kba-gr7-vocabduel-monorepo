import { TokenData } from './token-data';
import { User } from './internal/user';

export class LoggedInUser extends User {
    readonly authTokens!: TokenData;
}
