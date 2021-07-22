import { Component, OnInit } from '@angular/core';
import { User } from '../../../model/internal/user';
import { AuthService } from '../../../services/auth.service';
import { VocabularyService } from '../../../services/vocabulary.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ComplexDialogManagementService } from '../../../services/complex-dialog-management.service';

@Component({
    selector: 'app-person-search-page',
    templateUrl: './person-search-page.component.html',
    styleUrls: ['./person-search-page.component.scss'],
})
export class PersonSearchPageComponent implements OnInit {
    currentUser!: User;
    initialSearchVal!: string;

    constructor(
        readonly dialogManagement: ComplexDialogManagementService,
        private readonly auth: AuthService,
        private readonly vocabulary: VocabularyService,
        private readonly router: Router,
        private readonly route: ActivatedRoute
    ) {}

    ngOnInit(): void {
        this.auth.currentUser$.subscribe((user) => {
            if (user) this.currentUser = user;
        });
        this.initialSearchVal =
            this.route.snapshot.queryParamMap.get('q') ?? '';
    }

    addQueryParam(q: string): void {
        this.router
            .navigate([], {
                relativeTo: this.route,
                queryParams: { q },
                queryParamsHandling: 'merge',
            })
            .then();
    }
}
