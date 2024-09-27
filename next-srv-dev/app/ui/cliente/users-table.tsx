"use client";
import { UsuarioHabilitado } from "@/app/lib/definitions";

export function UsersTable({ usuarios }: { usuarios: UsuarioHabilitado[] }) {
  if (!usuarios)
    return (
      <div>
        <h1>No hay usuarios habilitados</h1>
      </div>
    );

  return (
    <div>
      <table className="w-full mt-2 border-2 border-white-800">
        <thead>
          <tr className="border-2 border-white-800">
            <th className="border-2 border-white-800">Nombre</th>
            <th className="hidden md:table-cell border-2 border-white-800">Correo</th>
          </tr>
        </thead>
        <tbody>
          {usuarios.map((usuario) => (
            <tr key={usuario.id}>
              <td className="p-2 border-l-2 border-white-800">
                {`${usuario.nombre} ${usuario.apellido}`}
              </td>
              <td className="hidden md:table-cell p-2 border-l-2 border-white-800">
                {usuario.correoElectronico}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
