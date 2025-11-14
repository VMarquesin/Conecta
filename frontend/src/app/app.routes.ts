import { Routes } from '@angular/router';

// 1. Importe os componentes que usaremos nas rotas
import { HomeComponent } from './pages/home/home';
import { PrestadorListComponent } from './components/prestador-list/prestador-list';
import { PrestadorDetailComponent } from './pages/prestador-detail/prestador-detail';
import { CadastroClienteComponent } from './pages/cadastro-cliente/cadastro-cliente';
import { CadastroPrestadorComponent } from './pages/cadastro-prestador/cadastro-prestador';
import { LoginComponent } from './pages/login/login';
import { MeuPerfilComponent } from './pages/meu-perfil/meu-perfil';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
  { path: '', component: HomeComponent, pathMatch: 'full' },
  { path: 'prestadores', component: PrestadorListComponent }, 
  { path: 'categoria/:nome', component: PrestadorListComponent },
  { path: 'prestador/:id', component: PrestadorDetailComponent },
  { path: 'cadastro', component: CadastroClienteComponent },
  { path: 'cadastro-prestador', component: CadastroPrestadorComponent },
  { path: 'login', component: LoginComponent },
  { 
      path: 'perfil', 
      component: MeuPerfilComponent,
      canActivate: [authGuard]
  }

];