import { Metadata } from "next";
import CreatePedidoFrom from "../../../ui/pedidos/create-form";
import { auth } from "@/auth";
import {
  fetchClienteByEmail,
  fetchObrasCliente,
  fetchUserByEmail,
} from "@/app/lib/data/cliente/data";
import { fetchProducts } from "@/app/lib/data/producto/data";
export const metadata: Metadata = {
  title: "Pedidos",
};

export default async function Page() {
  const session = await auth();
  const cliente = await fetchClienteByEmail(session?.user?.email);
  const UsuarioHabilitado = await fetchUserByEmail(session?.user?.email);
  const obras = await fetchObrasCliente(session?.user?.email);
  const obrasFlat = [
    ...obras.obrasHabilitadas,
    ...obras.obrasPendientes,
  ];
  const productos = await fetchProducts({});
  return (
    <main className="flex flex-col items-center justify-center w-full">
      <h1>Formulario de pedido</h1>
      <CreatePedidoFrom
        cliente={cliente}
        usuario={UsuarioHabilitado}
        obras={obrasFlat}
        productos={productos}
      />
    </main>
  );
}
