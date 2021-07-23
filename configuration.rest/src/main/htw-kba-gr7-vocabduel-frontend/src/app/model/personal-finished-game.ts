import { User } from './internal/user';

export class PersonalFinishedGame {
    id!: number;
    opponent!: User;
    gameResult!: 'WIN' | 'LOSS' | 'DRAW';
    ownPoints!: number;
    opponentPoints!: number;
    finishedTimestamp!: Date;
    learntLanguage!: string;
    knownLanguage!: string;
}
