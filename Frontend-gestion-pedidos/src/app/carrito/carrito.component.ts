import { Component, OnInit } from '@angular/core';
import { Producto } from '../productos/productos-response.model';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ApiService } from '../../api.service';

@Component({
  selector: 'app-carrito',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './carrito.component.html',
  styleUrl: './carrito.component.css'
})

export class CarritoComponent implements OnInit {
  productosSeleccionados: Producto[] = [];
  contadorCarrito: number = 0; 

  constructor(public router: Router, private apiService: ApiService) { }
  ngOnInit(): void {
    const productos = localStorage.getItem('productosEnCarrito');
    if (productos) {
      this.productosSeleccionados = JSON.parse(productos);
    }
    this.updateCartCount(); // Llama al método para actualizar el conteo
  }

  updateCartCount(): void {
    this.contadorCarrito = this.productosSeleccionados.length; // Actualiza el contador
  }

  async generarPedido() {
    const userData = localStorage.getItem('userData');
    let clienteId = 0; // Valor por defecto, reemplaza según tu lógica

    if (userData) {
      const user = JSON.parse(userData);
      clienteId = user.id; // Obtén el clienteId del usuario
    }

    if (this.productosSeleccionados.length === 0) {
      console.error('No hay productos seleccionados.');
      return; // Manejo de error: no hay productos en el carrito
    }

    const pedidoData = {
      clienteId: clienteId,
      productos: this.productosSeleccionados.map(producto => producto.id) // Obtén solo los IDs de los productos seleccionados
    };

    try {
      const respuesta = await this.apiService.generarPedido(pedidoData);
      console.log('Pedido generado:', respuesta);
      // Aquí puedes redirigir a otra página o mostrar un mensaje de éxito
      localStorage.removeItem('productosEnCarrito'); // Limpia el carrito
      this.router.navigate(['/productos']); // Redirige a la vista de pedido
    } catch (error) {
      console.error('Error al generar el pedido', error);
      // Aquí puedes manejar el error (por ejemplo, mostrar un mensaje al usuario)
    }
  }
}
