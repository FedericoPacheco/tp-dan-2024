"use client";
import { State } from "@/app/lib/actions/pedido/actions";
import { createPedido } from "@/app/lib/actions/pedido/actions";
import {
  Cliente,
  Obra,
  Producto,
  UsuarioHabilitado,
} from "@/app/lib/definitions";
import {
  CubeIcon,
  PlusIcon,
  TableCellsIcon,
  DocumentTextIcon,
  CurrencyDollarIcon,
} from "@heroicons/react/24/outline";
import { useActionState, useState } from "react";
import { Button } from "../button";
import Link from "next/link";

export default function CreatePedidoFrom({
  cliente,
  usuario,
  obras,
  productos,
}: {
  cliente: Cliente;
  usuario: UsuarioHabilitado;
  obras: Obra[];
  productos: Producto[];
}) {
  const initialState: State = { message: null, errors: {} };
  const [state, formAction] = useActionState(createPedido, initialState);
  const [detalles, setDetalles] = useState<{ idProducto: string; cantidad: number }[]>([]);
  const [total, setTotal] = useState(0.0);
  const handleAddProduct = () => {
    const selectElement = document.getElementById(
      "producto"
    ) as HTMLSelectElement;
    const selectedValue = selectElement?.value;
    console.log(selectedValue);
    const cantidadElement = document.getElementById(
      "cantidad"
    ) as HTMLInputElement;
    const cantidad = Number(cantidadElement?.value);
    if (!selectedValue || cantidad === 0) {
      console.log("no selecciono valores");
    } else {
      const producto = productos.find(
        (producto) => producto.id === Number(selectedValue)
      );

      if (!producto) {
        console.error("Producto no encontrado");
        return;
      }
      const precioTotal = producto.precio * cantidad;
      const detalle = {
        idProducto: selectedValue,
        cantidad: cantidad,
      };
      const newDetalles = [...detalles];
      newDetalles.push(detalle);
      setDetalles(newDetalles);
      setTotal(total + precioTotal);
    }
  };
  return (
    <form action={formAction} className="p-2 border-2 w-3/4">
      <input
        id="cliente"
        name="cliente"
        type="string"
        hidden={true}
        readOnly={true}
        value={cliente.id}
      />
      <input
        id="usuarioHabilitado"
        name="usuarioHabilitado"
        type="string"
        hidden={true}
        readOnly={true}
        value={usuario.id}
      />
      <div className="mb-4">
        <label htmlFor="obra" className="mb-2 block text-sm font-medium">
          Obra
        </label>
        <div className="relative">
          <select
            id="obra"
            name="obra"
            className="peer block w-full cursor-pointer rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
            aria-describedby="obra-error"
          >
            <option value="">Seleccione una obra</option>
            {obras.map((obra) => (
              <option key={obra.id} value={obra.id}>
                {obra.direccion}
              </option>
            ))}
          </select>
          <TableCellsIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500" />
        </div>
      </div>
      <div className="mb-4">
        <label htmlFor="producto" className="mb-2 block text-sm font-medium">
          Productos
        </label>
        <div className="flex">
          <div className="relative mr-2 w-full">
            <select
              id="producto"
              className="peer block w-full cursor-pointer rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
              aria-describedby="producto-error"
            >
              <option value="">Seleccione productos</option>
              {productos.map((producto) => (
                <option key={producto.id} value={producto.id} >
                  {producto.nombre}
                </option>
              ))}
            </select>
            <CubeIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
          </div>
          <div className="mr-2">
            <input
              id="cantidad"
              type="number"
              placeholder="cantidad"
              className="peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
              aria-describedby="cantidad-error"
              defaultValue={0.0}
            />
          </div>
          <Button
            type="button"
            className="ml-auto border-2 border-ebony-400"
            onClick={handleAddProduct}
          >
            <PlusIcon className="h-8 w-8" />
          </Button>
        </div>
        <div>
          <input
            id="prodSeleccionados"
            name="prodSeleccionados"
            type="array"
            className="peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
            aria-describedby="prodSeleccionados-error"
            readOnly={true}
            value={JSON.stringify(detalles)}
          ></input>
        </div>
      </div>
      <div className="mb-4">
        <label htmlFor="total" className="mb-2 block text-sm font-medium">
          Total
        </label>
        <div className="relative">
          <input
            id="total"
            name="total"
            type="number"
            className="peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
            aria-describedby="nombre-error"
            readOnly={true}
            value={total}
            defaultValue={0}
          />
          <CurrencyDollarIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
        </div>
      </div>
      <div className="mb-4">
        <label htmlFor="direccion" className="mb-2 block text-sm font-medium">
          Observaciones
        </label>
        <div className="relative mt-2 rounded-md">
          <div className="relative">
            <textarea
              id="observaciones"
              name="observaciones"
              placeholder="observaciones del pedido"
              className="peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
              aria-describedby="observaciones-error"
            />
            <DocumentTextIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
          </div>
        </div>
      </div>
      <div className="flex justify-center py-2 space-x-6">
        <Button className="border-2 p-2" type="submit">
          Crear Pedido
        </Button>
        <Link href={"/dashboard/pedidos"}>
          <Button className="border-2 p-2">Cancelar</Button>
        </Link>
      </div>
      {state.message && <p>{state.message}</p>}
    </form>
  );
}
