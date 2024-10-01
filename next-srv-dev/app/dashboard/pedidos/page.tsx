import { Button } from "@/app/ui/button";
import { lusitana } from "@/app/ui/fonts/fonts";
import PedidoClienteMenu from "@/app/ui/pedidos/pedidos-cliente";
import PedidosGeneral from "@/app/ui/pedidos/pedidos-general";
import { auth } from "@/auth";
import { PlusIcon } from "@heroicons/react/24/outline";
import { Metadata } from "next";
import Link from "next/link";

export const metadata: Metadata = {
  title: "Pedidos",
};

export default async function Page() {
  const session = await auth();
  if (!session) return null;
  const isAdmin = session?.user?.email === "admin@admin.com";

  return (
    <main>
      <div className="flex bg-white-200 p-2 m-2 border-b-2 border-black">
        <h1
          className={`${lusitana.className} sm:text-[12px] md:text-[22px] p-2`}
        >
          Info de pedidos :
        </h1>
        <div className="flex items-center ml-auto">
          {!isAdmin && (
            <Link className="ml-auto mr-4" href={"/dashboard/pedidos/create"}>
            <Button className="border-2 border-ebony-400">
              <p className="p-2">Nuevo Pedido</p>
              <PlusIcon className="h-8 w-8" />
            </Button>
          </Link>
          )}
          <h1 className="sm:text-[12px] md:text-[22px]">{`Cuenta de ${session.user?.name}`}</h1>
        </div>
      </div>
      <div className="w-full">
        {isAdmin && <PedidosGeneral />}
        {!isAdmin && <PedidoClienteMenu />}
      </div>
    </main>
  );
}
