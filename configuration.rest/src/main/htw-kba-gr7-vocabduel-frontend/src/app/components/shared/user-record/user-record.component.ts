import { Component, Input, OnInit } from '@angular/core';
import { ScoreService } from '../../../services/score.service';
import { Observable } from 'rxjs';
import { ScoreRecord } from '../../../model/score-record';
import { User } from '../../../model/internal/user';

@Component({
    selector: 'app-user-record',
    templateUrl: './user-record.component.html',
    styleUrls: ['./user-record.component.scss'],
})
export class UserRecordComponent implements OnInit {
    @Input() user?: User;
    record$!: Observable<ScoreRecord>;

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

    constructor(private readonly score: ScoreService) {}

    ngOnInit(): void {
        this.record$ = this.user
            ? this.score.userRecord$(this.user)
            : this.score.ownRecord$;
    }
}
