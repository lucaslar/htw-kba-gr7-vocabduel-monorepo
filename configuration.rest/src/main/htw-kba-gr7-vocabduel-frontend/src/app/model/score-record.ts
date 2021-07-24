import { User } from './internal/user';

export class ScoreRecord {
    user!: User;
    wins!: number;
    losses!: number;
    draws!: number;
}
