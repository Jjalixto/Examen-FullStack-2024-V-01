export interface PedidoResponse {
    clienteId: number;
    pedidoId: number;
    productos: number[];
    total: number;
    fechaPedido: string;
}