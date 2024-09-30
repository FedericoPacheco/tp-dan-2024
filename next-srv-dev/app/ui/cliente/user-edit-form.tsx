"use client";
import { UsuarioHabilitado } from "@/app/lib/definitions";
import {
  AtSymbolIcon,
  BuildingOfficeIcon,
  IdentificationIcon,
} from "@heroicons/react/24/outline";
import { useActionState } from "react";
import { Button } from "../button";
import { editUsuario, StateUser } from "@/app/lib/actions/cliente/actions";

export function UserEditForm({
  usuario,
}: {
  usuario: UsuarioHabilitado | null;
}) {
  if (!usuario) {
    usuario = {} as UsuarioHabilitado;
  }
  const initialState: StateUser = { message: null, errors: {} };
  const [state, formAction] = useActionState(editUsuario, initialState);

  return (
    <form action={formAction}>
      <div className="rounded-md p-4">
        <div className="flex w-full">
          <div className="w-full m-2">
            <label
              className="mb-3 mt-5 block text-xs font-medium"
              htmlFor="nombre"
            >
              Nombre
            </label>
            <div className="relative">
              <input
                className="peer block w-full rounded-md border border-gray-200 py-[9px] pl-10 text-sm outline-2 placeholder:text-gray-500"
                id="nombre"
                type="nombre"
                name="nombre"
                placeholder="Ingrese nombre de cliente"
                defaultValue={usuario.nombre}
              />
              <BuildingOfficeIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
            </div>
          </div>
          <div className="w-full m-2">
            <label
              className="mb-3 mt-5 block text-xs font-medium"
              htmlFor="apellido"
            >
              Apellido
            </label>
            <div className="relative">
              <input
                className="peer block w-full rounded-md border border-gray-200 py-[9px] pl-10 text-sm outline-2 placeholder:text-gray-500"
                id="apellido"
                type="apellido"
                name="apellido"
                placeholder="Ingrese apellido de cliente"
                defaultValue={usuario.apellido}
              />
              <AtSymbolIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
            </div>
          </div>
        </div>
        <div className="flex w-full">
          <div className="w-full m-2">
            <label
              className="mb-3 mt-5 block text-xs font-medium"
              htmlFor="cuit"
            >
              DNI
            </label>
            <div className="relative">
              <input
                className="peer block w-full rounded-md border border-gray-200 py-[9px] pl-10 text-sm outline-2 placeholder:text-gray-500"
                id="dni"
                type="dni"
                name="dni"
                placeholder="Ingrese dni, sin puntos ni lineas"
                defaultValue={usuario.dni}
              />
              <IdentificationIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
            </div>
          </div>
          <div className="w-full m-2">
            <label
              className="mb-3 mt-5 block text-xs font-medium"
              htmlFor="correoElectronico"
            >
              Correo Electronico
            </label>
            <div className="relative">
              <input
                className="peer block w-full rounded-md border border-gray-200 py-[9px] pl-10 text-sm outline-2 placeholder:text-gray-500"
                id="correoElectronico"
                type="correoElectronico"
                name="correoElectronico"
                placeholder="Ingrese correo electronico"
                defaultValue={usuario.correoElectronico}
              />
              <AtSymbolIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
            </div>
          </div>
        </div>
        <div>
          {state !== undefined && state.message && (
            <div className="flex justify-center text-red-500 text-sm">
              {state.message}
            </div>
          )}
        </div>
        <div className="flex justify-center py-2 space-x-6">
          <Button className="border-2 p-2" type="submit">
            Editar Usuario
          </Button>
        </div>
      </div>
    </form>
  );
}
