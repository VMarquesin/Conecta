import { Routes } from '@angular/router';
import { CadastroClienteComponent } from './pages/cadastro-cliente/cadastro-cliente';
import { CadastroPrestadorComponent } from './pages/cadastro-prestador/cadastro-prestador';
import { PrestadorListComponent } from './components/prestador-list/prestador-list';

// 1. Importe o novo componente de login
import { LoginComponent } from './pages/login/login';

export const routes: Routes = [
    { path: '', component: PrestadorListComponent, pathMatch: 'full' },
    { path: 'cadastro', component: CadastroClienteComponent },
    { path: 'cadastro-prestador', component: CadastroPrestadorComponent },
    
    // 2. Adicione a rota de login
    { path: 'login', component: LoginComponent }
];