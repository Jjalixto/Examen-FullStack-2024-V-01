import { Component, OnInit } from '@angular/core';
import { PedidoResponse } from './pedido-response.model'; // Asegúrate de importar tu modelo
import { ApiService } from '../../api.service'; // Importa tu servicio
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-pedido',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pedido.component.html',
  styleUrls: ['./pedido.component.css']
})
export class PedidoComponent implements OnInit {
  pedidoResponse: PedidoResponse | null = null;
  clienteId: number = 0; // Inicializa el clienteId
  pedidos: PedidoResponse[] = []; // Array para almacenar los pedidos

  constructor(
    private apiService: ApiService, 
    private route: ActivatedRoute,
    public redirect: Router
    ) { } // Asegúrate de inyectar ActivatedRoute aquí

    ngOnInit(): void {
      this.route.params.subscribe(params => {
        this.clienteId = +params['clienteId']; // Obtén el clienteId
        this.cargarPedidos(); // Método para cargar los pedidos
        this.guardarPedido(); // Mueve esta línea aquí
      });
    }

  async guardarPedido() {
    // Aquí recuperas el clienteId y los productos del localStorage
    const userData = localStorage.getItem('userData');
    const productosEnCarrito = localStorage.getItem('productosEnCarrito');

    if (userData && productosEnCarrito) {
      const user = JSON.parse(userData);
      const productos = JSON.parse(productosEnCarrito).map((producto: { id: number }) => producto.id); // Obtén solo los IDs

      const pedidoData = {
        clienteId: user.id, // Usa el ID del usuario
        productos: productos // Usa los IDs de los productos
      };

      try {
        this.pedidoResponse = await this.apiService.generarPedido(pedidoData);
        console.log('Pedido generado:', this.pedidoResponse);
      } catch (error) {
        console.error('Error al mostrar el pedido', error);
      }
    }
  }

  async cargarPedidos() {
    try {
      this.pedidos = await this.apiService.obtenerPedidosPorCliente(this.clienteId);
      console.log('Pedidos cargados:', this.pedidos);
    } catch (error) {
      console.error('Error al cargar los pedidos', error);
      // Aquí puedes manejar el error (por ejemplo, mostrar un mensaje al usuario)
    }
  }
}
