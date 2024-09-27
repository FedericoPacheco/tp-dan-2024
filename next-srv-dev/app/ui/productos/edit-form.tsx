"use client";

import { useActionState } from "react";
import {
  TableCellsIcon,
  PlusCircleIcon,
  MinusCircleIcon,
  CubeIcon,
  IdentificationIcon,
} from "@heroicons/react/20/solid";
import { Button } from "../button";
import Link from "next/link";
import { Producto } from "../../lib/definitions";
import { editProduct, StateCreate } from "@/app/lib/actions/producto/actions";

export default function EditProduct({ producto }: { producto: Producto }) {
  const initialState: StateCreate = { message: null, errors: {} };
  const [state, formAction] = useActionState(editProduct, initialState);
  return (
    <form action={formAction}>
      <div className="rounded-md p-4">
        {/* ID de producto - oculto*/}
        <input
          id="producto"
          name="producto"
          type="string"
          placeholder=""
          className="hidden peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
          aria-describedby="nombre-error"
          hidden={true}
          value={producto.id}
          readOnly={true}
        />
        {/* Nombre */}
        <div className="mb-4">
          <label htmlFor="producto" className="mb-2 block text-sm font-medium">
            Nombre de Producto
          </label>
          <div className="relative mt-2 rounded-md">
            <div className="relative">
              <input
                id="nombre"
                name="nombre"
                type="string"
                defaultValue={producto.nombre}
                placeholder="Ingrese el nombre del producto"
                className="peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
                aria-describedby="nombre-error"
              />
              <CubeIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
            </div>
          </div>
        </div>
        {/* Descripción */}
        <div className="mb-4">
          <label
            htmlFor="descripcion"
            className="mb-2 block text-sm font-medium"
          >
            Descripción
          </label>
          <div className="relative mt-2 rounded-md">
            <div className="relative">
              <textarea
                id="descripcion"
                name="descripcion"
                defaultValue={producto.descripcion}
                placeholder="Ingrese una descripción del producto"
                className="peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
                aria-describedby="descripcion-error"
              />
              <IdentificationIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
            </div>
          </div>
        </div>
        {/* Precio */}
        <div className="mb-4">
          <label htmlFor="precio" className="mb-2 block text-sm font-medium">
            Precio
          </label>
          <div className="relative mt-2 rounded-md">
            <div className="relative">
              <input
                id="precio"
                name="precio"
                type="number"
                defaultValue={producto.precio}
                placeholder="Ingrese el precio del producto"
                className="peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
                aria-describedby="precio-error"
              />
              <MinusCircleIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
            </div>
          </div>
        </div>
        {/* Descuento Promocional */}
        <div className="mb-4">
          <label
            htmlFor="descuentoPromocional"
            className="mb-2 block text-sm font-medium"
          >
            Descuento Promocional
          </label>
          <div className="relative mt-2 rounded-md">
            <div className="relative">
              <input
                id="descuentoPromocional"
                name="descuentoPromocional"
                type="number"
                defaultValue={producto.descuentoPromocional}
                placeholder="Ingrese el descuento promocional"
                className="peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
                aria-describedby="descuentoPromocional-error"
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
            Editar
          </Button>
          <Link href={"/dashboard/productos"}>
            <Button className="border-2 p-2">Cancelar</Button>
          </Link>
        </div>
      </div>
    </form>
  );
}
