import { Component, Input } from '@angular/core';
import { ScoreRecord } from '../../../model/score-record';

@Component({
    selector: 'app-user-record',
    templateUrl: './user-record.component.html',
    styleUrls: ['./user-record.component.scss'],
})
export class UserRecordComponent {
    @Input() record!: ScoreRecord;

    readonly colorWin = {
        backgroundColor: 'rgba(138,186,24,0.5)',
        borderColor: '#8aba18',
        hoverBackgroundColor: 'rgba(138,186,24,0.75)',
    };

    readonly colorLoss = {
        backgroundColor: 'rgba(186,24,24,0.5)',
        borderColor: '#BA1818',
        hoverBackgroundColor: 'rgba(186,24,24,0.75)',
    };

    readonly colorDraw = {
        backgroundColor: 'rgba(24,78,186,0.5)',
        borderColor: '#184eba',
        hoverBackgroundColor: 'rgba(24,78,186,0.75)',
    };
}
