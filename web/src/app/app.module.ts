import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {ChartsModule} from 'ng2-charts';

import {AppComponent} from './app.component';
import {DatePipe} from '@angular/common';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    ChartsModule
  ],
  providers: [DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
