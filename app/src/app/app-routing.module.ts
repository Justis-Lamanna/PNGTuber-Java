import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SubmitComponent } from './submit/submit.component';
import { PngTuberComponent } from './png-tuber/png-tuber.component';

const routes: Routes = [
  { path: '', component: SubmitComponent, pathMatch: 'full' },
  { path: '/pngtuber/:id', component: PngTuberComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
