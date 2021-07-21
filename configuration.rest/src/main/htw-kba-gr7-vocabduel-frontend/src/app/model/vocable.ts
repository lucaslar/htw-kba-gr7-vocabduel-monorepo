import { TranslationGroup } from './translation-group';

export class Vocable {
    id!: number;
    translations!: TranslationGroup[];
    vocable!: TranslationGroup;
    exampleOrAdditionalInfoLearntLang?: string;
    exampleOrAdditionalInfoKnownLang?: string;
}
