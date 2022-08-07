import { NgModule } from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {ItemsComponent} from "./items/items.component";

const routes: Routes = [
  {path: "", redirectTo: "/items", pathMatch: "full"},
  {path: "items", component: ItemsComponent},
  // { path: 'detail/:id', component: ItemsComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
