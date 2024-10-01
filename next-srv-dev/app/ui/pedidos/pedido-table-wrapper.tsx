import { Pedido } from "@/app/lib/definitions";
import PedidosTable from "./pedidos-table";
import { lusitana } from "../fonts/fonts";

export default function PedidoTableWrapper({
  pedidos, label
}: {
  pedidos: Pedido[], label: string;
}) {
  return (
    <div className="bg-white-200 p-2 m-2 w-full">
      <h1
        className={`${lusitana.className} sm:text-[12px] md:text-[22px] p-2 border-b-2 border-black`}
      >
        {`${label}`}
      </h1>
      <PedidosTable pedidos={pedidos} />
    </div>
  );
}
