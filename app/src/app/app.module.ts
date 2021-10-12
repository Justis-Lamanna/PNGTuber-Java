import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SubmitComponent } from './submit/submit.component';
import { DragAndDropComponent } from './drag-and-drop/drag-and-drop.component';
import { PngTuberComponent } from './png-tuber/png-tuber.component';

@NgModule({
  declarations: [
    AppComponent,
    SubmitComponent,
    DragAndDropComponent,
    PngTuberComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
