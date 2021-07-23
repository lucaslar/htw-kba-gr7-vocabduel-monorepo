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
import { PlayGameComponent } from './components/main/play-game/play-game.component';
import { GameAccessResolver } from './guards/game-access.resolver';
import { StartGameComponent } from './components/main/start-game/start-game.component';

const routes: Routes = [
    {
        path: 'registration',
        component: RegistrationComponent,
        canActivate: [UnauthorizedGuard],
        data: { animation: 'Registration' },
    },
    {
        path: 'login',
        component: LoginComponent,
        canActivate: [UnauthorizedGuard],
        data: { animation: 'Login' },
    },
    {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [AuthGuard],
        data: { animation: 'Dashboard' },
    },
    {
        path: 'settings',
        component: SettingsComponent,
        canActivate: [AuthGuard],
        data: { animation: 'Settings' },
    },
    {
        path: 'vocabulary',
        component: VocabularyComponent,
        data: { animation: 'Vocabulary' },
    },
    {
        path: 'user-search',
        component: PersonSearchPageComponent,
        data: { animation: 'UserSearch' },
    },
    {
        path: 'play/start',
        component: StartGameComponent,
        data: { animation: 'StartGame' },
        canActivate: [AuthGuard],
    },
    {
        path: 'play/:gameId',
        component: PlayGameComponent,
        canActivate: [AuthGuard],
        resolve: { data: GameAccessResolver },
        data: { animation: 'PlayGame' },
    },
    { path: '**', redirectTo: 'dashboard' },
];

@NgModule({
    imports: [RouterModule.forRoot(routes, { onSameUrlNavigation: 'reload' })],
    exports: [RouterModule],
})
export class AppRoutingModule {}
