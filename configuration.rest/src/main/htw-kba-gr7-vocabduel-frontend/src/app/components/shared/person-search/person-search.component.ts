import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../../../model/internal/user';
import { UserService } from '../../../services/user.service';
import { NavigationService } from '../../../services/navigation.service';

@Component({
    selector: 'app-person-search',
    templateUrl: './person-search.component.html',
    styleUrls: ['./person-search.component.scss'],
})
export class PersonSearchComponent {
    @Input() ownId?: number;
    @Output() readonly userSelected: EventEmitter<User> = new EventEmitter();

    searchStr = '';
    results$?: Observable<User[]>;
    lastEmittedStr?: string;

    constructor(
        readonly user: UserService,
        readonly navigation: NavigationService
    ) {}
}
