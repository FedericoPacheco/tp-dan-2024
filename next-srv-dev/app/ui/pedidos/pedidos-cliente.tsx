import { fetchClienteByEmail } from "@/app/lib/data/cliente/data";
import { fetchPedidosSegmentadosByCliente } from "@/app/lib/data/pedido/data";
import { Cliente, PedidoSegmentado } from "@/app/lib/definitions";
import { auth } from "@/auth";
import PedidoTableWrapper from "./pedido-table-wrapper";

export default async function PedidoClienteMenu() {
  const session = await auth();
  const cliente: Cliente = await fetchClienteByEmail(session?.user?.email);
  const pedidoSegmentado: PedidoSegmentado =
    await fetchPedidosSegmentadosByCliente(cliente);

  return (
    <div className="w-full">
      <PedidoTableWrapper pedidos={pedidoSegmentado.pedidosRecibidos} label="Pedidos Recibidos"/>
      <PedidoTableWrapper pedidos={pedidoSegmentado.pedidosAceptados} label="Pedidos Aceptados"/>
      <PedidoTableWrapper pedidos={pedidoSegmentado.pedidosRechazados} label="Pedidos Rechazados"/>
      <PedidoTableWrapper pedidos={pedidoSegmentado.pedidosCancelados} label="Pedidos Cancelados"/>
      <PedidoTableWrapper pedidos={pedidoSegmentado.pedidosEnPreparacion} label="Pedidos en Preparacion"/>
      <PedidoTableWrapper pedidos={pedidoSegmentado.pedidosEntregados} label="Pedidos Entregados"/>
    </div>
  );
}
