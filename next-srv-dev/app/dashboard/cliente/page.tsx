import {
  fetchClienteByEmail,
  fetchClientes,
  fetchUserByEmail,
  fetchUsers,
} from "@/app/lib/data/cliente/data";
import { ClienteEditForm } from "@/app/ui/cliente/cliente-edit-form";
import { UsersTable } from "@/app/ui/cliente/users-table";
import { lusitana } from "@/app/ui/fonts/fonts";
import { auth } from "@/auth";
import { Metadata } from "next";
import UserInfo from "../../ui/cliente/user-info";
import { UserEditForm } from "../../ui/cliente/user-edit-form";
import { Cliente, UsuarioHabilitado } from "@/app/lib/definitions";
import Link from "next/link";
import { Button } from "@/app/ui/button";
import { PlusIcon } from "@heroicons/react/24/outline";
import UserTableContainer from '../../ui/cliente/usuario-habilitado-table-container';

export const metadata: Metadata = {
  title: "Cliente",
};

export default async function Page() {
  const session = await auth();
  //Funciona, pero esta muy atado con alambre.
  const isAdmin = session?.user?.email === "admin@admin.com";

  const client: Cliente | null = !isAdmin
    ? await fetchClienteByEmail(session?.user?.email)
    : null;

  const user: UsuarioHabilitado | null = !isAdmin
    ? await fetchUserByEmail(session?.user?.email)
    : null;
  //El "default user" tiene mas permisos que un usuario comun, pero menos que un admin.
  const isDefaultUser =
    !isAdmin && client?.correoElectronico === user?.correoElectronico;

  const users: UsuarioHabilitado[] = isDefaultUser
    ? await fetchUsers(client)
    : [];
  const clients: Cliente[] = isAdmin ? await fetchClientes() : [];
  return (
    <main>
      <div>
        <div className="bg-white-200 p-2 m-2">
          <h1
            className={`${lusitana.className} sm:text-[12px] md:text-[22px] p-2 border-b-2 border-black`}
          >
            Info de usuario :
          </h1>
          <UserInfo
            session={session}
            isDefaultUser={isDefaultUser}
            isAdmin={isAdmin}
          />
        </div>
        {!isAdmin && (
          <div>
            <div className="bg-white-200 p-2 m-2">
              <h1
                className={`${lusitana.className} sm:text-[12px] md:text-[22px] p-2 border-b-2 border-black`}
              >
                Usuario habilitado :
              </h1>
              <UserEditForm usuario={user} />
            </div>
            <div className="bg-white-200 p-2 m-2">
              <h1
                className={`${lusitana.className} sm:text-[12px] md:text-[22px] p-2 border-b-2 border-black`}
              >
                Cliente :
              </h1>
              <ClienteEditForm cliente={client} isDefaultUser={isDefaultUser} />
            </div>
          </div>
        )}
        {isDefaultUser && (
          <div className="bg-white-200 p-2 m-2">
            <div className="flex items-center w-full">
              <h1
                className={`${lusitana.className} sm:text-[12px] md:text-[22px] p-2 border-b-2 border-black `}
              >
                Usuarios habilitados :
              </h1>
              <div className="flex w-3/4">
                <Link className="ml-auto mr-4" href={"/dashboard/cliente"}>
                  <Button className="border-2 border-ebony-400">
                    <p className="p-2">Nuevo usuario</p>
                    <PlusIcon className="h-10 w-10" />
                  </Button>
                </Link>
              </div>
            </div>
            <UsersTable usuarios={users} />
          </div>
        )}
        {isAdmin &&
          clients.map(async (cliente) => 
            (
            <div className="bg-white-200 p-2 m-2">
              <ClienteEditForm cliente={cliente} isDefaultUser={false} />
              <UserTableContainer usuarios={await fetchUsers(cliente)}/>
            </div>
          ))}
      </div>
    </main>
  );
}
