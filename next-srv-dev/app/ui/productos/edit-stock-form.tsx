"use client";

import { StateEditStock,editStock } from "@/app/lib/actions/producto/actions";
import { useActionState } from "react";
import {
  TableCellsIcon,
  PlusCircleIcon,
} from "@heroicons/react/20/solid";
import { Button } from "../button";
import Link from "next/link";
import { Producto } from '../../lib/definitions';

export default function AñadirStock({productos}:{productos: Producto[]}) {
  const initialState: StateEditStock = { message: null, errors: {} };
  const [state, formAction] = useActionState(editStock, initialState);
  return (
    <form action={formAction}>
      <div className="rounded-md p-4">
        {/* Producto */}
        <div className="mb-4">
          <label htmlFor="producto" className="mb-2 block text-sm font-medium">
            Producto
          </label>
          <div className="relative">
            <select
              id="producto"
              name="producto"
              className="peer block w-full cursor-pointer rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
              aria-describedby="producto-error"
            >
              <option value="">Seleccione una categoria de productos</option>
              {productos.map((product) => (
                <option key={product.id} value={product.id}>
                  {product.nombre}
                </option>
              ))}
            </select>
            <TableCellsIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500" />
          </div>
        </div>
        {/* Stock */}
        <div className="mb-4">
          <label
            htmlFor="stock"
            className="mb-2 block text-sm font-medium"
          >
            Añadir Stock
          </label>
          <div className="relative mt-2 rounded-md">
            <div className="relative">
              <input
                id="stock"
                name="stock"
                type="number"
                min={0}
                step={1}
                placeholder="ingrese el nuevo stock"
                className="peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
                aria-describedby="stock-error"
              />
              <PlusCircleIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
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
            Añadir stock
          </Button>
          <Link href={"/dashboard/productos"}>
            <Button className="border-2 p-2">Cancelar</Button>
          </Link>
        </div>
      </div>
    </form>
  );
}
