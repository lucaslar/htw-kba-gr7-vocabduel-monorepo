import { ErrorHandler, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import {
    HTTP_INTERCEPTORS,
    HttpClient,
    HttpClientModule,
} from '@angular/common/http';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { MaterialModule } from './modules/material.module';
import { HeaderComponent } from './components/header/header.component';
import { EllipsisTooltipDirective } from './directives/ellipsis-tooltip.directive';
import { DashboardComponent } from './components/main/dashboard/dashboard.component';
import { SidenavContentComponent } from './components/sidenav-content/sidenav-content.component';
import { ErrorService } from './services/error.service';
import { ErrorDialogComponent } from './components/dialogs/error-dialog/error-dialog.component';
import { LoginComponent } from './components/main/login/login.component';
import { RegistrationComponent } from './components/main/registration/registration.component';
import { JWT_OPTIONS, JwtHelperService } from '@auth0/angular-jwt';
import { FormsModule } from '@angular/forms';
import { AuthInterceptor } from './helpers/auth-interceptor';
import { SettingsComponent } from './components/main/settings/settings.component';
import { PersonSearchComponent } from './components/shared/person-search/person-search.component';
import { PersonSearchPageComponent } from './components/main/person-search-page/person-search-page.component';
import { VocabularyComponent } from './components/main/vocabulary/vocabulary.component';
import { FlagForLangPipe } from './pipes/flag-for-lang.pipe';
import { LanguageReferencesComponent } from './components/dialogs/language-references/language-references.component';
import { NgxDropzoneModule } from 'ngx-dropzone';
import { ConfirmDeleteComponent } from './components/dialogs/confirm-delete/confirm-delete.component';
import { VocabularyListComponent } from './components/dialogs/vocabulary-list/vocabulary-list.component';
import { OverlappingFlagsComponent } from './components/shared/overlapping-flags/overlapping-flags.component';
import { TranslationGroupComponent } from './components/dialogs/vocabulary-list/translation-group/translation-group.component';
import { UserDetailsComponent } from './components/dialogs/user-details/user-details.component';
import { LanguageSetsAccordionComponent } from './components/shared/language-sets-accordion/language-sets-accordion.component';
import { LangSetsByVocabularyPipe } from './pipes/lang-sets-by-vocabulary.pipe';
import { OpenGamesComponent } from './components/main/dashboard/open-games/open-games.component';
import { PlayGameComponent } from './components/main/play-game/play-game.component';

const HttpLoaderFactory = (http: HttpClient) => {
    return new TranslateHttpLoader(http, './assets/i18n/', '.json');
};

@NgModule({
    declarations: [
        AppComponent,
        HeaderComponent,
        EllipsisTooltipDirective,
        DashboardComponent,
        SidenavContentComponent,
        ErrorDialogComponent,
        LoginComponent,
        RegistrationComponent,
        SettingsComponent,
        PersonSearchComponent,
        PersonSearchPageComponent,
        VocabularyComponent,
        FlagForLangPipe,
        LanguageReferencesComponent,
        ConfirmDeleteComponent,
        VocabularyListComponent,
        OverlappingFlagsComponent,
        TranslationGroupComponent,
        UserDetailsComponent,
        LanguageSetsAccordionComponent,
        LangSetsByVocabularyPipe,
        OpenGamesComponent,
        PlayGameComponent,
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: HttpLoaderFactory,
                deps: [HttpClient],
            },
        }),
        MaterialModule,
        FormsModule,
        NgxDropzoneModule,
    ],
    providers: [
        {
            provide: ErrorHandler,
            useClass: ErrorService,
        },
        { provide: JWT_OPTIONS, useValue: JWT_OPTIONS },
        JwtHelperService,
        { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    ],
    bootstrap: [AppComponent],
})
export class AppModule {}
