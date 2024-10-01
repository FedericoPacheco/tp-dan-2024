import { fetchPedidosSegmentados } from "@/app/lib/data/pedido/data";
import {PedidoSegmentado } from "../../lib/definitions";
import PedidoTableWrapper from "./pedido-table-wrapper";

export default async function PedidosGeneral() {
  const pedidoSegmentado: PedidoSegmentado = await fetchPedidosSegmentados();
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
