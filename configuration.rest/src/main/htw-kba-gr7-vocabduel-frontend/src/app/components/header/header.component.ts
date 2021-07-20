import { Component, HostListener } from '@angular/core';
import { NavigationService } from '../../services/navigation.service';

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
    isScrolled: boolean = false;

    constructor(readonly navigation: NavigationService) {}

    @HostListener('window:scroll') onScroll(): void {
        this.isScrolled = window.pageYOffset > 0;
    }
}
