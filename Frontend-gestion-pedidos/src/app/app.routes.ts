import { Routes } from '@angular/router';
import { ProductosComponent } from './productos/productos.component';
import { LoginComponent } from './login/login.component';
import { CarritoComponent } from './carrito/carrito.component';
import { PedidoComponent } from './pedido/pedido.component';
import { EditarPedidoComponent } from './editar-pedido/editar-pedido.component';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },  // Ruta para el login
    { path: 'productos', component: ProductosComponent },  // Ruta para productos
    { path: 'carrito', component: CarritoComponent },
    { path: 'editar-pedido/:pedidoId', component: EditarPedidoComponent },
    { path: 'pedido', component: PedidoComponent },
    { path: '', redirectTo: 'login', pathMatch: 'full' },  // Redirige a login si no se especifica ruta
    { path: '**', redirectTo: 'login' }
];
