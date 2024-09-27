import { fetchClienteByEmail, fetchClientes } from "@/app/lib/data/cliente/data";
import CreateObraForm from "@/app/ui/dashboard/obra-create-form";
import CreateObraFormAdmin from "@/app/ui/dashboard/obra-create-form-admin";
import { lusitana } from "@/app/ui/fonts/fonts";
import { auth } from "@/auth";

export default async function Page() {
    const session = await auth();
    //Funciona, pero esta muy atado con alambre.
    const isAdmin = session?.user?.email === "admin@admin.com";

    const clientes = isAdmin ? await fetchClientes() : [];

    const cliente = !isAdmin ? await fetchClienteByEmail(session?.user?.email) : undefined;
    
    return (
      <main className="flex flex-col items-center justify-center bg-white-100">
        <h1
          className={`${lusitana.className} text-center text-[22px] text-black-50 py-2`}
        >
          <strong>Nueva Obra</strong>
        </h1>
        {isAdmin ? <CreateObraFormAdmin clientes={clientes}/> : <CreateObraForm cliente={cliente}/>} 
      </main>
    );
  }