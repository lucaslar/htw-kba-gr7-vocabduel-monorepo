import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/main/dashboard/dashboard.component';
import { LoginComponent } from './components/main/login/login.component';
import { RegistrationComponent } from './components/main/registration/registration.component';
import { AuthGuard } from './guards/auth.guard';
import { UnauthorizedGuard } from './guards/unauthorized.guard';
import { SettingsComponent } from './components/main/settings/settings.component';
import { PersonSearchPageComponent } from './components/main/person-search-page/person-search-page.component';
import { VocabularyComponent } from './components/main/vocabulary/vocabulary.component';

const routes: Routes = [
    {
        path: 'registration',
        component: RegistrationComponent,
        canActivate: [UnauthorizedGuard],
    },
    {
        path: 'login',
        component: LoginComponent,
        canActivate: [UnauthorizedGuard],
    },
    {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [AuthGuard],
    },
    { path: 'settings', component: SettingsComponent },
    { path: 'vocabulary', component: VocabularyComponent },
    { path: 'user-search', component: PersonSearchPageComponent },
    { path: '**', redirectTo: 'dashboard' },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {}
