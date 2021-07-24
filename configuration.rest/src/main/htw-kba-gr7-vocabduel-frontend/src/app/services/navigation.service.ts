import { Injectable } from '@angular/core';
import { MediaMatcher } from '@angular/cdk/layout';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
import { StorageService } from './storage.service';
import { MatDialog } from '@angular/material/dialog';
import { LanguageSelectionComponent } from '../components/dialogs/language-selection/language-selection.component';
import { Language } from '../model/internal/language';

type NavItem = {
    icon: string;
    translationKey: string;
    colorClass: string;
    onClick: () => void;
};

@Injectable({
    providedIn: 'root',
})
export class NavigationService {
    isLoadingIndicated = true;
    isSidebarOpened = false;

    private readonly sharedNavItems: NavItem[] = [
        {
            icon: 'school',
            translationKey: 'header.actions.vocabulary',
            colorClass: 'color-primary',
            onClick: async () => this.navigateAndClose('vocabulary'),
        },
        {
            icon: 'person_search',
            translationKey: 'header.actions.searchPerson',
            colorClass: 'color-primary',
            onClick: async () => this.navigateAndClose('user-search'),
        },
        {
            icon: 'language',
            translationKey: 'header.actions.language',
            colorClass: 'color-primary',
            onClick: () => this.dialog.open(LanguageSelectionComponent),
        },
    ];

    private readonly navigationItemsLoggedIn: NavItem[] = [
        {
            icon: 'dashboard',
            translationKey: 'header.actions.home',
            colorClass: 'color-primary',
            onClick: async () => this.navigateAndClose('dashboard'),
        },
        ...this.sharedNavItems,
        {
            icon: 'settings',
            translationKey: 'header.actions.settings',
            colorClass: 'color-primary',
            onClick: async () => this.navigateAndClose('settings'),
        },
        {
            icon: 'power_settings_new',
            translationKey: 'header.actions.logout',
            colorClass: 'color-warn',
            onClick: () => this.auth.logout(),
        },
    ];

    private readonly navigationItemsLoggedOut: NavItem[] = [
        {
            icon: 'login',
            translationKey: 'header.actions.login',
            colorClass: 'color-accent',
            onClick: async () => this.navigateAndClose('login'),
        },
        ...this.sharedNavItems,
    ];

    private readonly mobileQuery: MediaQueryList;

    constructor(
        media: MediaMatcher,
        private readonly router: Router,
        private readonly auth: AuthService,
        private readonly storage: StorageService,
        private readonly dialog: MatDialog
    ) {
        this.mobileQuery = media.matchMedia('(max-width: 576px)');
    }

    checkMediaQuery(): void {
        if (!this.mobileQuery.matches) {
            this.isSidebarOpened = false;
        }
    }

    get navigationItems(): NavItem[] {
        return this.storage.refreshToken && this.storage.token
            ? this.navigationItemsLoggedIn
            : this.navigationItemsLoggedOut;
    }

    private async navigateAndClose(path: string): Promise<void> {
        this.isSidebarOpened = false;
        await this.router.navigate([path]);
    }
}
