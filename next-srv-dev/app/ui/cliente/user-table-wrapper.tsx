import { UsuarioHabilitado } from "@/app/lib/definitions";
import UserTableContainer from "./usuario-habilitado-table-container";

export default async function UserTableWrapper({
  usuariosPromise,
}: {
  usuariosPromise: Promise<UsuarioHabilitado[]>;
}) {
  const usuarios = await usuariosPromise;

  
  return (
    <div className="flex flex-col items-center rounded-lg bg-white-200 p-2 mb-4 border-2">
      <UserTableContainer usuarios={usuarios} />
    </div>
  );
}
