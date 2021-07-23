import { TranslationGroup } from './translation-group';

export class CorrectAnswerResult {
    result!: 'WIN' | 'LOSS';
    correctAnswer!: TranslationGroup;
}
