import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'firstObjectKey',
})
export class FirstObjectKeyPipe implements PipeTransform {
    transform(value: any): string {
        return Object.keys(value)[0];
    }
}
