import { lusitana } from "@/app/ui/fonts/fonts";
import { Metadata } from "next";
import { auth } from "auth";
import ObrasSet from "@/app/ui/dashboard/obras-set";
import { fetchObras, fetchObrasCliente } from "@/app/lib/data/cliente/data";
import { ObraSegmentada } from "@/app/lib/definitions";
import Link from "next/link";
import { Button } from "../../ui/button";
import { PlusIcon } from "@heroicons/react/20/solid";

export const metadata: Metadata = {
  title: "Dashboard",
};

export default async function Page() {
  const session = await auth();
  const isAdmin = session?.user?.email === "admin@admin.com";

  const obrasSegmentada: ObraSegmentada = isAdmin
    ? await fetchObras()
    : await fetchObrasCliente(session?.user?.email);

  return (
    <main>
      {/* cliente / usuario */}
      <div className="mb-4">
        <div className="flex items-center rounded-lg bg-white-200 h-16 md:h-20 p-2 pl-4 border-2 w-full">
          {!isAdmin && (
            <h1
              className={`${lusitana.className} sm:text-[12px] md:text-[22px] w-1/4`}
            >
              {`Bienvenido ${session?.user?.name}! 🎉`}
            </h1>
          )}
          {isAdmin && (
            <h1
              className={`${lusitana.className} sm:text-[12px] md:text-[22px]`}
            >
              <strong>{`Perfil de Administrador`}</strong>
            </h1>
          )}
          <div className="flex w-3/4">
            <Link className="ml-auto mr-4" href={"/dashboard/obra/create"}>
              <Button className="border-2 border-ebony-400">
                <p className="p-2">Crear Obra </p>
                <PlusIcon className="h-10 w-10" />
              </Button>
            </Link>
          </div>
        </div>
      </div>
      <ObrasSet
        obrasHabilitadas={obrasSegmentada.obrasHabilitadas}
        obrasPendientes={obrasSegmentada.obrasPendientes}
        obrasFinalizadas={obrasSegmentada.obrasFinalizadas}
      />
    </main>
  );
}
