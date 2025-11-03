import { Routes } from '@angular/router';
import { CadastroClienteComponent } from './pages/cadastro-cliente/cadastro-cliente';
// 1. Importe o novo componente
import { CadastroPrestadorComponent } from './pages/cadastro-prestador/cadastro-prestador';
import { PrestadorListComponent } from './components/prestador-list/prestador-list';

export const routes: Routes = [
    { path: '', component: PrestadorListComponent, pathMatch: 'full' },
    { path: 'cadastro', component: CadastroClienteComponent },
    // 2. Adicione a nova rota
    { path: 'cadastro-prestador', component: CadastroPrestadorComponent }
];