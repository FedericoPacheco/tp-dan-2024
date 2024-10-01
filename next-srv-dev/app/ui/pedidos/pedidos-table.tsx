"use client";

import { DetallePedido, Pedido } from "../../lib/definitions";

export default function PedidosTable({ pedidos }: { pedidos: Pedido[] }) {
  return (
    <table className="w-full">
      <thead>
        <tr>
          <th>Id</th>
          <th>Fecha</th>
          <th>Estado</th>
          <th>Productos</th>
        </tr>
      </thead>
      <tbody>
        {pedidos.map((pedido: Pedido) => (
          <tr key={pedido.id}>
            <td>{pedido.id}</td>
            <td>{pedido.fecha}</td>
            <td>{pedido.estado}</td>
            <td>
              {pedido.detalles.map((detalle: DetallePedido) => (
                <p key={detalle.idProducto}>
                  Cod: {detalle.idProducto} , Cant: {detalle.cantidad}
                </p>
              ))}
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
