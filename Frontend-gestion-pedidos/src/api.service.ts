import axios from 'axios';
import { Injectable } from '@angular/core';
import { LoginResponse } from './app/login/login-response.model';
import { Producto } from './app/productos/productos-response.model';
import { PedidoResponse } from './app/pedido/pedido-response.model';

@Injectable({
    providedIn: 'root'
})
export class ApiService {
    private baseUrl = 'http://localhost:8080/gestion_pedidos'; // Cambia esto por la URL de tu API

    constructor() { }

    async login(email: string, password: string): Promise<LoginResponse> {
        try {
            const response = await axios.post<LoginResponse>(`${this.baseUrl}/login`, {
                email,
                password
            });
            return response.data;
        } catch (error) {
            console.error('Error al iniciar sesión', error);
            throw error; // Puedes manejar el error como prefieras
        }
    }

    async getProductos(): Promise<Producto[]> {
        try {
            const response = await axios.get<Producto[]>(`${this.baseUrl}/producto`);
            return response.data;
        } catch (error) {
            console.error('Error al obtener productos', error);
            throw error; // Puedes manejar el error como prefieras
        }
    }

    async generarPedido(pedidoData: { clienteId: number; productos: number[] }): Promise<PedidoResponse> {
        try {
            const response = await axios.post<PedidoResponse>(`${this.baseUrl}/pedido/crear`, pedidoData);
            return response.data;
        } catch (error) {
            console.error('Error al generar pedido', error);
            throw error;
        }
    }

    async obtenerPedidosPorCliente(clienteId: number): Promise<PedidoResponse[]> {
        try {
            const response = await axios.get<PedidoResponse[]>(`${this.baseUrl}/pedido/cliente/${clienteId}/pedidos`); // Cambia la URL según tu API
            return response.data;
        } catch (error) {
            console.error('Error al obtener los pedidos', error);
            throw error;
        }
    }

    async obtenerPedidoPorId(pedidoId: number): Promise<PedidoResponse> {
        try {
            const response = await axios.get<PedidoResponse>(`${this.baseUrl}/pedido/${pedidoId}`);
            return response.data;
        } catch (error) {
            console.error('Error al obtener el pedido', error);
            throw error;
        }
    }

    async actualizarPedido(pedidoId: number, clienteId: number, pedidoData: PedidoResponse): Promise<PedidoResponse> {
        try {
            const response = await axios.put<PedidoResponse>(`${this.baseUrl}/pedido/actualizar/${clienteId}/${pedidoId}`, pedidoData);
            return response.data;
        } catch (error) {
            console.error('Error al actualizar el pedido', error);
            throw error;
        }
    }
}
