"use client";
import { editCliente, StateClient } from "@/app/lib/actions/cliente/actions";
import { Cliente } from "@/app/lib/definitions";
import {
  AtSymbolIcon,
  BuildingOfficeIcon,
  IdentificationIcon,
  PlusCircleIcon,
} from "@heroicons/react/24/outline";
import { useActionState } from "react";
import { Button } from "../button";

export function ClienteEditForm({
  cliente,
  isDefaultUser,
}: {
  cliente: Cliente | null;
  isDefaultUser: boolean;
}) {
  if (!cliente) {
    cliente = {} as Cliente;
  }
  const initialState: StateClient = { message: null, errors: {} };
  const [state, formAction] = useActionState(editCliente, initialState);

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
                defaultValue={cliente.nombre}
                disabled={!isDefaultUser}
              />
              <BuildingOfficeIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
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
                defaultValue={cliente.correoElectronico}
                disabled={!isDefaultUser}
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
              CUIT
            </label>
            <div className="relative">
              <input
                className="peer block w-full rounded-md border border-gray-200 py-[9px] pl-10 text-sm outline-2 placeholder:text-gray-500"
                id="cuit"
                type="cuit"
                name="cuit"
                placeholder="Ingrese cuit de la empresa, sin puntos ni lineas"
                defaultValue={cliente.cuit}
                disabled={!isDefaultUser}
              />
              <IdentificationIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
            </div>
          </div>
          <div className="w-full m-2">
            <label
              className="mb-3 mt-5 block text-xs font-medium"
              htmlFor="maximoDescubierto"
            >
              Maximo Descubierto
            </label>
            <div className="relative">
              <input
                className="peer block w-full rounded-md border border-gray-200 py-[9px] pl-10 text-sm outline-2 placeholder:text-gray-500"
                id="maximoDescubierto"
                type="maximoDescubierto"
                name="maximoDescubierto"
                placeholder="Ingrese maximo descubierto"
                defaultValue={cliente.maximoDescubierto}
                disabled={!isDefaultUser}
              />
              <PlusCircleIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
            </div>
          </div>
        </div>
        <div className="flex w-full">
          <div className="w-full m-2">
            <label
              className="mb-3 mt-5 block text-xs font-medium"
              htmlFor="maximaCantidadObrasEnEjecucion"
            >
              Maxima Cantidad Obras En Ejecucion
            </label>
            <div className="relative">
              <input
                className="peer block w-full rounded-md border border-gray-200 py-[9px] pl-10 text-sm outline-2 placeholder:text-gray-500"
                id="maximaCantidadObrasEnEjecucion"
                type="maximaCantidadObrasEnEjecucion"
                name="maximaCantidadObrasEnEjecucion"
                placeholder="Ingrese maxima cantidad de obras en ejecucion"
                defaultValue={cliente.maximaCantidadObrasEnEjecucion}
                disabled={!isDefaultUser}
              />
              <PlusCircleIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
            </div>
          </div>
          <div className="w-full m-2">
            <label
              className="mb-3 mt-5 block text-xs font-medium"
              htmlFor="descubierto"
            >
              Descubierto
            </label>
            <div className="relative">
              <input
                className="peer block w-full rounded-md border border-gray-200 py-[9px] pl-10 text-sm outline-2 placeholder:text-gray-500"
                id="descubierto"
                type="descubierto"
                name="descubierto"
                placeholder="Ingrese descubierto"
                defaultValue={cliente.descubierto}
                disabled={!isDefaultUser}
              />
              <PlusCircleIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
            </div>
          </div>
        </div>
        <div id="message" aria-live="polite" aria-atomic="true">
          {state.message && <p className="mt-2 text-sm text-green-500">{state.message}</p>}
        </div>
        <div className="flex justify-center py-2 space-x-6">
          <Button
            className="cursor-not-allowed border-2 p-2"
            type="submit"
            disabled={!isDefaultUser}
          >
            Editar Cliente
          </Button>
        </div>
      </div>
    </form>
  );
}
