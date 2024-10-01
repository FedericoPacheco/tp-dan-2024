import { Cliente, Pedido, PedidoSegmentado } from "../../definitions";

const urlGeneral = process.env.NEXT_PUBLIC_GW_ADDR;

export async function fetchPedidosSegmentados(): Promise<PedidoSegmentado> {
    try {
        const url = `${urlGeneral}/pedidos/api/pedidos`;
        const response = await fetch(url);
        if (!response.ok) throw new Error('Failed to fetch pedidos data.');

        const pedidos: Pedido[] = await response.json()

        const recibidos: Pedido[] = pedidos.filter(pedido => pedido.estado === 'RECIBIDO');
        const aceptados: Pedido[] = pedidos.filter(pedido => pedido.estado === 'ACEPTADO');
        const rechazados: Pedido[] = pedidos.filter(pedido => pedido.estado === 'RECHAZADO');
        const cancelados: Pedido[] = pedidos.filter(pedido => pedido.estado === 'CANCELADO');
        const enPreparacion: Pedido[] = pedidos.filter(pedido => pedido.estado === 'EN_PREPARACION');
        const entregados: Pedido[] = pedidos.filter(pedido => pedido.estado === 'ENTREGADO');

        const pedidoSegmentado: PedidoSegmentado = {
            pedidosRecibidos: recibidos,
            pedidosAceptados: aceptados,
            pedidosRechazados: rechazados,
            pedidosCancelados: cancelados,
            pedidosEnPreparacion: enPreparacion,
            pedidosEntregados: entregados
        };
        return pedidoSegmentado;
    } catch (error) {
        console.error('API Error:', error);
        throw new Error('Failed to fetch pedidos data.');
    }
}
export async function fetchPedidosSegmentadosByCliente(cliente:Cliente): Promise<PedidoSegmentado> {
    try {
        const url = `${urlGeneral}/pedidos/api/pedidos/cliente/${cliente.id}`;
        const response = await fetch(url);
        if (!response.ok) throw new Error('Failed to fetch pedidos data.');

        const pedidos: Pedido[] = await response.json()

        const recibidos: Pedido[] = pedidos.filter(pedido => pedido.estado === 'RECIBIDO');
        const aceptados: Pedido[] = pedidos.filter(pedido => pedido.estado === 'ACEPTADO');
        const rechazados: Pedido[] = pedidos.filter(pedido => pedido.estado === 'RECHAZADO');
        const cancelados: Pedido[] = pedidos.filter(pedido => pedido.estado === 'CANCELADO');
        const enPreparacion: Pedido[] = pedidos.filter(pedido => pedido.estado === 'EN_PREPARACION');
        const entregados: Pedido[] = pedidos.filter(pedido => pedido.estado === 'ENTREGADO');

        const pedidoSegmentado: PedidoSegmentado = {
            pedidosRecibidos: recibidos,
            pedidosAceptados: aceptados,
            pedidosRechazados: rechazados,
            pedidosCancelados: cancelados,
            pedidosEnPreparacion: enPreparacion,
            pedidosEntregados: entregados
        };
        return pedidoSegmentado;
    } catch (error) {
        console.error('API Error:', error);
        throw new Error('Failed to fetch pedidos data.');
    }
}