import { Component, OnInit } from '@angular/core';
import { User } from '../../../model/internal/user';
import { AuthService } from '../../../services/auth.service';

@Component({
    selector: 'app-person-search-page',
    templateUrl: './person-search-page.component.html',
    styleUrls: ['./person-search-page.component.scss'],
})
export class PersonSearchPageComponent implements OnInit {
    currentUser!: User;

    constructor(private readonly auth: AuthService) {}

    ngOnInit(): void {
        this.auth.currentUser$.subscribe((user) => {
            if (user) this.currentUser = user;
        });
    }

    displayUserDetails(user: User): void {
        // TODO Implement
        console.log(user);
    }
}
