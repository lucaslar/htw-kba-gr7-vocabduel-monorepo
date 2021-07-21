import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../../../model/internal/user';
import { UserService } from '../../../services/user.service';
import { NavigationService } from '../../../services/navigation.service';

@Component({
    selector: 'app-person-search',
    templateUrl: './person-search.component.html',
    styleUrls: ['./person-search.component.scss'],
})
export class PersonSearchComponent implements OnInit {
    @Input() ownId?: number;
    @Input() searchStr = '';
    @Output() readonly userSelected: EventEmitter<User> = new EventEmitter();
    @Output() readonly afterSearch: EventEmitter<string> = new EventEmitter();

    results$?: Observable<User[]>;
    lastEmittedStr?: string;

    constructor(
        readonly navigation: NavigationService,
        private readonly user: UserService
    ) {}

    ngOnInit(): void {
        if (this.searchStr?.trim()) this.search();
    }

    search(): void {
        this.results$ = this.user.findUsers$(this.searchStr);
        this.lastEmittedStr = this.searchStr;
        this.afterSearch.emit(this.searchStr);
    }
}
