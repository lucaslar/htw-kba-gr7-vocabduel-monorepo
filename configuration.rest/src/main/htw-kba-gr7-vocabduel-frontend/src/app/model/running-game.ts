import { User } from './internal/user';
import { VocableList } from './vocable-list';

export class RunningGame {
    id!: number;
    opponent!: User;
    totalRounds!: number;
    finishedRoundsSelf!: number;
    finishedRoundsOpponent!: number;
    knownLanguage!: string;
    learntLanguage!: string;
    usedVocabularyLists!: VocableList[];
}
