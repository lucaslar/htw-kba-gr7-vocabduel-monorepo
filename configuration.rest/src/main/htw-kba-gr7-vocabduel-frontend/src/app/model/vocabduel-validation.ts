import { AbstractControl, ValidationErrors } from '@angular/forms';

export const pwdVal = (control: AbstractControl): ValidationErrors | null => {
    let nrOfNum = 0;
    let nrOfLower = 0;
    let nrOfUpper = 0;
    let nrOfSpecial = 0;

    [...(control.value as string)].forEach((c) => {
        if (c.match('\\d+')) nrOfNum++;
        else if (c.match('[a-z]+')) nrOfLower++;
        else if (c.match('[A-Z]+')) nrOfUpper++;
        else if (c.match('(?=.*[-+_!@#$%^&*., ?]).+')) nrOfSpecial++;
    });

    return [nrOfLower, nrOfUpper, nrOfNum, nrOfSpecial].filter((x) => x !== 0)
        .length > 2
        ? null
        : { pwdNotValid: true };
};

export const nameValidation = (
    control: AbstractControl
): ValidationErrors | null => {
    return (control.value as string).trim() &&
        (control.value as string)
            .trim()
            .split('\\s')
            .every(
                (n) =>
                    n[0] === n[0].toUpperCase() &&
                    [...n.slice(1)].every((x) => x.toLowerCase() === x)
            )
        ? null
        : { nameInvalid: true };
};
