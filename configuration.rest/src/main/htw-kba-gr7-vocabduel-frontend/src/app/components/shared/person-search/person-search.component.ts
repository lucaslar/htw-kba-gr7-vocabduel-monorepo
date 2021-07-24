import {
    Component,
    ElementRef,
    EventEmitter,
    Input,
    OnInit,
    Output,
    ViewChild,
} from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../../../model/internal/user';
import { UserService } from '../../../services/user.service';
import { NavigationService } from '../../../services/navigation.service';
import { map } from 'rxjs/operators';

@Component({
    selector: 'app-person-search',
    templateUrl: './person-search.component.html',
    styleUrls: ['./person-search.component.scss'],
})
export class PersonSearchComponent implements OnInit {
    @Input() ownId?: number;
    @Input() searchStr = '';
    @Input() exclude?: number;
    @Output() readonly userSelected: EventEmitter<User> = new EventEmitter();
    @Output() readonly afterSearch: EventEmitter<string> = new EventEmitter();

    @ViewChild('clearButton', { read: ElementRef })
    private readonly clearButton!: ElementRef;
    @ViewChild('searchInput') private readonly searchInput!: ElementRef;

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
        this.results$ = this.user.findUsers$(this.searchStr).pipe(
            map((users) => {
                return this.exclude !== undefined
                    ? users.filter((u) => u.id !== this.exclude)
                    : users;
            })
        );
        this.lastEmittedStr = this.searchStr;
        this.afterSearch.emit(this.searchStr);
    }

    get isSearchbarElementActive(): boolean {
        return (
            document.activeElement === this.searchInput?.nativeElement ||
            document.activeElement === this.clearButton?.nativeElement
        );
    }
}
