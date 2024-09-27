"use client";

import { useActionState } from "react";
import {
  TableCellsIcon,
  MinusCircleIcon,
  PlusCircleIcon,
  IdentificationIcon,
} from "@heroicons/react/20/solid";
import { Button } from "../button";
import Link from "next/link";
import { createObra, State } from "@/app/lib/actions/obra/actions";
import { Cliente } from "@/app/lib/definitions";

export default function CreateObraForm({ clientes }: { clientes: Cliente[] }) {
  const initialState: State = { message: null, errors: {} };
  const [state, formAction] = useActionState(createObra, initialState);
  return (
    <form action={formAction}>
      <div className="rounded-md p-4">
        {/* Cliente */}
        <div className="mb-4">
          <label htmlFor="cliente" className="mb-2 block text-sm font-medium">
            Cliente
          </label>
          <div className="relative">
            <select
              id="cliente"
              name="cliente"
              className="peer block w-full cursor-pointer rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
              aria-describedby="cliente-error"
            >
              <option value="">Seleccione un cliente para la nueva obra</option>
              {clientes.map((cliente) => (
                <option key={cliente.id} value={cliente.id}>
                  {cliente.nombre}
                </option>
              ))}
            </select>
            <TableCellsIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500" />
          </div>
        </div>
        {/* Direccion */}
        <div className="mb-4">
          <label htmlFor="direccion" className="mb-2 block text-sm font-medium">
            Dirección
          </label>
          <div className="relative mt-2 rounded-md">
            <div className="relative">
              <textarea
                id="direccion"
                name="direccion"
                placeholder="Ingrese la direccion de la obra"
                className="peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
                aria-describedby="direccion-error"
              />
              <IdentificationIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
            </div>
          </div>
        </div>
        {/* Latitud */}
        <div className="mb-4">
          <label htmlFor="latitud" className="mb-2 block text-sm font-medium">
            Latitud
          </label>
          <div className="relative mt-2 rounded-md">
            <div className="relative">
              <input
                id="latitud"
                name="latitud"
                type="number"
                placeholder="Ingrese la latitud"
                className="peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
                aria-describedby="latitud-error"
              />
              <PlusCircleIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
            </div>
          </div>
        </div>
        {/* Longitud */}
        <div className="mb-4">
          <label htmlFor="longitud" className="mb-2 block text-sm font-medium">
            Longitud
          </label>
          <div className="relative mt-2 rounded-md">
            <div className="relative">
              <input
                id="longitud"
                name="longitud"
                type="number"
                placeholder="Ingrese la longitud"
                className="peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
                aria-describedby="longitud-error"
              />
              <MinusCircleIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
            </div>
          </div>
        </div>
        {/* Presupuesto */}
        <div className="mb-4">
          <label htmlFor="presupuesto" className="mb-2 block text-sm font-medium">
            Presupuesto
          </label>
          <div className="relative mt-2 rounded-md">
            <div className="relative">
              <input
                id="presupuesto"
                name="presupuesto"
                type="number"
                placeholder="Ingrese el presupuesto"
                className="peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
                aria-describedby="presupuesto-error"
              />
              <MinusCircleIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
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
            Crear obra
          </Button>
          <Link href={"/dashboard"}>
            <Button className="border-2 p-2">Cancelar</Button>
          </Link>
        </div>
      </div>
    </form>
  );
}
