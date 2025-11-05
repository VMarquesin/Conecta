import { Routes } from '@angular/router';
import { CadastroClienteComponent } from './pages/cadastro-cliente/cadastro-cliente';
import { CadastroPrestadorComponent } from './pages/cadastro-prestador/cadastro-prestador';
import { PrestadorListComponent } from './components/prestador-list/prestador-list';
import { LoginComponent } from './pages/login/login';

export const routes: Routes = [
    { path: '', component: PrestadorListComponent, pathMatch: 'full' },
    { path: 'cadastro', component: CadastroClienteComponent },
    { path: 'cadastro-prestador', component: CadastroPrestadorComponent },
    { path: 'login', component: LoginComponent }
];