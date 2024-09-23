import { Component, OnInit } from '@angular/core';
import { Producto } from './productos-response.model';
import { ApiService } from '../../api.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './productos.component.html',
  styleUrl: './productos.component.css'
})
export class ProductosComponent implements OnInit {
  nombre: string = '';
  id: number = 0;
  productos: Producto[] = [];
  contadorCarrito: number = 0;

  constructor(private apiService: ApiService, public router: Router) { }
  async ngOnInit(): Promise<void> {
    const userData = localStorage.getItem('userData');
    if (userData) {
      const user = JSON.parse(userData);
      console.log(user);
      this.nombre = user.nombre;
      this.id = user.id;
    }

    try {
      this.productos = await this.apiService.getProductos();
    } catch (error) {
      console.error('Error en la obtencion de productos', error);
    }

    this.updateCartCount();
  }

  updateCartCount(): void {
    const productosEnCarrito = localStorage.getItem('productosEnCarrito');
    if (productosEnCarrito) {
      const productos = JSON.parse(productosEnCarrito);
      this.contadorCarrito = productos.length; // Actualiza el contador
    }
  }

  addToCar(producto: Producto) {
    if (producto.stock > 0) {
      this.contadorCarrito++; // Incrementa el contador
      producto.stock--;

      const productosEnCarrito = JSON.parse(localStorage.getItem('productosEnCarrito') || '[]');
      productosEnCarrito.push(producto);
      localStorage.setItem('productosEnCarrito', JSON.stringify(productosEnCarrito));
    } else {
      console.log('No hay stock disponible para el producto:', producto.nombre);
    }
  }

  verPedidos() {
    const userData = localStorage.getItem('userData');
    let clienteId = 0;

    if (userData) {
      const user = JSON.parse(userData);
      clienteId = user.id; // Obtén el clienteId del usuario
    }

    // Redirige a la vista de pedidos, pasando el clienteId como parámetro
    this.router.navigate(['/pedido', {clienteId}]);
  }

  cerrarSesion() {
    localStorage.removeItem('userData'); // Elimina los datos del usuario
    localStorage.removeItem('productosEnCarrito'); // Opcional: elimina los productos del carrito
    this.router.navigate(['/login']); // Redirige a la página de login
  }
}
