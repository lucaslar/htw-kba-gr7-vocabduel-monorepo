import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'flagForLang',
})
export class FlagForLangPipe implements PipeTransform {
    transform(value: string): string {
        if (value === 'JA') value = 'JP';
        else if (value === 'HI') value = 'IN';
        else if (value === 'KO') value = 'KR';
        else if (value === 'CMN') value = 'CN';
        else if (value === 'EN') value = 'GB';
        else if (value === 'AR') value = 'LB';
        return `flag-icon-${value.toLowerCase()}`;
    }
}
