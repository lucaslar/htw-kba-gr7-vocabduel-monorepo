import { User } from './internal/user';
import { Vocable } from './vocable';

export class VocableList {
    id!: number;
    author!: User;
    title!: string;
    timestamp!: Date;
    vocables!: Vocable[];
}
