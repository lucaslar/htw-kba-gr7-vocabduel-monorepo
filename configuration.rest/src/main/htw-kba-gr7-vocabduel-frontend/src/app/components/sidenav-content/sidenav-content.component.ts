import { Component } from '@angular/core';
import { NavigationService } from '../../services/navigation.service';

@Component({
    selector: 'app-sidenav-content',
    templateUrl: './sidenav-content.component.html',
    styleUrls: ['./sidenav-content.component.scss'],
})
export class SidenavContentComponent {
    constructor(readonly navigation: NavigationService) {}
}
