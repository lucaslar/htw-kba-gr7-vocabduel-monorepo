import { TranslationGroup } from './translation-group';

export class VocabduelRound {
    id!: number;
    roundNr!: number;
    question!: { id: number; vocable: TranslationGroup };
    answers!: TranslationGroup[];
}
