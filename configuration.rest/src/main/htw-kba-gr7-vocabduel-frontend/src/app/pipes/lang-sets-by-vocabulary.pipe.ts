import { Pipe, PipeTransform } from '@angular/core';
import { VocableList } from '../model/vocable-list';
import { LanguageSet } from '../model/language-set';

@Pipe({
    name: 'langSetsByVocabulary',
})
export class LangSetsByVocabularyPipe implements PipeTransform {
    transform(value: VocableList[]): LanguageSet[] {
        return value
            .map((v) => {
                return {
                    learntLanguage: v.learntLanguage,
                    knownLanguage: v.knownLanguage,
                    vocableUnits: [
                        {
                            title: v.unitTitle,
                            vocableLists: [v],
                        },
                    ],
                } as LanguageSet;
            })
            .reduce((prev: LanguageSet[], curr) => {
                const existingLs = prev.find(
                    (ls) =>
                        ls.learntLanguage === curr.learntLanguage &&
                        ls.knownLanguage === curr.knownLanguage
                );

                if (!existingLs) prev.push(curr);
                else {
                    const currUnit = curr.vocableUnits[0];
                    const existingUnit = existingLs.vocableUnits.find(
                        (u) => u.title === currUnit.title
                    );
                    if (existingUnit) {
                        existingUnit.vocableLists.push(
                            currUnit.vocableLists[0]
                        );
                    } else existingLs.vocableUnits.push(currUnit);
                }
                return prev;
            }, [])
            .sort((a, b) => {
                return a.knownLanguage + a.learntLanguage <
                    b.knownLanguage + b.learntLanguage
                    ? -1
                    : 1;
            });
    }
}
