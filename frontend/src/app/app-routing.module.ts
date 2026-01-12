import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { TaskListComponent } from './tasks/task-list/task-list.component';
import { AuthGuard } from './guards/auth.guard';
import { LoginGuard } from './guards/login.guard';

const routes: Routes = [
  { path: 'login', component: LoginComponent  , canActivate: [LoginGuard] },
  { path: 'tasks', component: TaskListComponent, canActivate: [AuthGuard] }, // Protect tasks route
  { path: '**', redirectTo: 'login' }, // Catch-all redirects to login
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
