import { NgModule, CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { MatToolbarModule } from '@angular/material/toolbar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, COMPOSITION_BUFFER_MODE } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { SlideMenuComponent } from './slide-menu/slide-menu.component';

import { ChatBoxComponent } from './chat-box/chat-box.component';
import { MemoBoxComponent } from './memo-box/memo-box.component';
import { ProjectPageComponent } from './project-page/project-page.component';
import { DirectoryPageComponent } from './directory-page/directory-page.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { FileUploadComponent } from './file-upload/file-upload.component';
import { CalendarPageComponent } from './calendar-page/calendar-page.component';

import { FileUploadModule} from 'ng2-file-upload';
import { MatTabsModule,MatCardModule,MatSelectModule, MatIconModule, MatFormFieldModule, MatInputModule } from '@angular/material';
import { PopupModule } from '@progress/kendo-angular-popup';
import { EscModule } from 'angular-esc';
// search module
import { Ng2SearchPipeModule } from 'ng2-search-filter';

// 드래그
import {DragDropModule} from '@angular/cdk/drag-drop';
import {MatButtonModule} from '@angular/material/button';
import {MatCheckboxModule} from '@angular/material/checkbox';
import { ColorPickerModule } from 'ngx-color-picker';
import { ColorPickerComponent } from '@syncfusion/ej2-angular-inputs';
import { MyPageComponent } from './my-page/my-page.component';
import { SocialLoginModule, AuthServiceConfig, GoogleLoginProvider } from 'angular4-social-login';

// 구글 로그인 관련
const google_oauth_client_id:string = '494035203334-17nlts7294itpjsoi12elupnbbt05r9k.apps.googleusercontent.com';
let config = new AuthServiceConfig([
  {
    id: GoogleLoginProvider.PROVIDER_ID,
    provider: new GoogleLoginProvider(google_oauth_client_id)
  }
]);

@NgModule({
  declarations: [
    LoginPageComponent,
    AppComponent,
    HeaderComponent,
    SlideMenuComponent,
    ChatBoxComponent,
    MemoBoxComponent,
    ProjectPageComponent,
    DirectoryPageComponent,
    FileUploadComponent,
    CalendarPageComponent,
    ColorPickerComponent,
    MyPageComponent,
  ],
  imports: [
    EscModule,
    FormsModule,
    ColorPickerModule,
    DragDropModule,
    MatButtonModule,
    MatCheckboxModule,
    Ng2SearchPipeModule,
    MatCardModule,
    MatIconModule,
    MatSelectModule,
    MatFormFieldModule,
    MatInputModule,
    MatTabsModule,
    FileUploadModule,
    PopupModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    BrowserModule,
    HttpClientModule,
    MatToolbarModule,
    SocialLoginModule.initialize(config),
  ],
    providers: [{provide: COMPOSITION_BUFFER_MODE, useValue: false }], 
  bootstrap: [AppComponent],
})
export class AppModule { }