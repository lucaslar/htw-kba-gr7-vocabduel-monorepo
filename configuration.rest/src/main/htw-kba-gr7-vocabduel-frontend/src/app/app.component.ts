import { Component, HostListener } from '@angular/core';
import { I18nService } from './services/i18n.service';
import { NavigationService } from './services/navigation.service';
import { slideAnimation } from './model/functions/route-animations';

@Component({
    animations: [slideAnimation],
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
})
export class AppComponent {
    constructor(i18n: I18nService, readonly navigation: NavigationService) {
        i18n.initialize();
    }

    @HostListener('window:resize') onResize(): void {
        this.navigation.checkMediaQuery();
    }
}
