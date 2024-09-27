'use client';

import { searchByFilters, StateFilter } from "@/app/lib/actions/producto/actions";
import { Categoria } from "@/app/lib/definitions";
import { useActionState } from 'react';
import { TableCellsIcon, MinusCircleIcon, PlusCircleIcon, CubeIcon, IdentificationIcon} from '@heroicons/react/20/solid';
import { Button } from "../button";

export default function FiltersForm({categories}:{categories:Categoria[]}) {
    //Por ahora tengo este conflicto de tipos, con el cual me va a costar trabajar los errores.
    //Tuve que colocar errors {...} | {} para que no me de error en la linea 10.
    const initialState: StateFilter = { message: null, errors: {} };
    const [state, formAction] = useActionState(searchByFilters, initialState);
    return(
        <form className="bg-white-200" action={formAction}>
            <div className="rounded-md p-4">
                {/* Categoria */}
                <div className="mb-4">
                    <label htmlFor="categoria" className="mb-2 block text-sm font-medium">
                        Elija una cateogría
                    </label>
                    <div className="relative">
                        <select
                            id="categoria"
                            name="categoria"
                            className="peer block w-full cursor-pointer rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
                            aria-describedby="categoria-error"
                        >
                            <option value="">
                                Seleccione una categoria de productos
                            </option>
                            {categories.map((category) => (
                                <option key={category.id} value={category.id}>
                                {category.nombre}
                                </option>
                            ))}
                        </select>
                        <TableCellsIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500" />
                    </div>
                </div>
                {/* Nombre */}
                <div className="mb-4">
                    <label htmlFor="producto" className="mb-2 block text-sm font-medium">
                        Escriba un nombre de Producto
                    </label>
                    <div className="relative mt-2 rounded-md">
                        <div className="relative">
                            <input
                                id="nombre"
                                name="nombre"
                                type="string"
                                placeholder="Ingrese el nombre del producto"
                                className="peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
                                aria-describedby='nombre-error'
                            />
                            <CubeIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
                        </div>
                    </div>
                </div>
                {/* Codigo */}
                <div className="mb-4">
                    <label htmlFor="codigo" className="mb-2 block text-sm font-medium">
                        Escriba un codigo de Producto
                    </label>
                    <div className="relative mt-2 rounded-md">
                        <div className="relative">
                            <input
                                id="codigo"
                                name="codigo"
                                type="string"
                                placeholder="Ingrese el codigo del producto"
                                className="peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
                                aria-describedby='codigo-error'
                            />
                            <IdentificationIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
                        </div>
                    </div>
                </div>
                {/* Rango precios */}
                <div className="mb-4">
                    <label htmlFor="codigo" className="mb-2 block text-sm font-medium">
                        Ingrese un rango de precios
                    </label>
                    <div className="flex justify-center gap-4 relative mt-2 rounded-md">
                        <div className="relative w-full">
                            <input
                                id="precioMin"
                                name="precioMin"
                                type="number"
                                step="0.01"
                                min={0.00}
                                placeholder="Ingrese el precio minimo del producto"
                                className="peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
                                aria-describedby='precioMin-error'
                            />
                            <MinusCircleIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
                        </div>
                        <div className="relative w-full">
                            <input
                                id="precioMax"
                                name="precioMax"
                                type="number"
                                step="0.01"
                                min={0.01}
                                placeholder="Ingrese el precio maximo del producto"
                                className="peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
                                aria-describedby='precioMax-error'
                            />
                            <PlusCircleIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
                        </div>
                    </div>
                </div>
                {/* Rango stock */}
                <div className="mb-4">
                    <label htmlFor="codigo" className="mb-2 block text-sm font-medium">
                        Ingrese un rango de stock
                    </label>
                    <div className="flex justify-center gap-4 relative mt-2 rounded-md">
                        <div className="relative w-full">
                            <input
                                id="stockMin"
                                name="stockMin"
                                type="number"
                                step="1"
                                min={0}
                                placeholder="Ingrese el stock minimo del producto"
                                className="peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
                                aria-describedby='stockMin-error'
                            />
                            <MinusCircleIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
                        </div>
                        <div className="relative w-full">
                            <input
                                id="stockMax"
                                name="stockMax"
                                type="number"
                                step="1"
                                min={1}
                                placeholder="Ingrese el codigo del producto"
                                className="peer block w-full rounded-md border border-gray-200 py-2 pl-10 text-sm outline-2 placeholder:text-gray-500"
                                aria-describedby='stockMax-error'   
                            />
                            <PlusCircleIcon className="pointer-events-none absolute left-3 top-1/2 h-[18px] w-[18px] -translate-y-1/2 text-gray-500 peer-focus:text-gray-900" />
                        </div>
                    </div>
                </div>
            </div>
            <div>
                {
                (state !== undefined) && state.message && 
                    (
                        <div className="flex justify-center text-red-500 text-sm">{state.message}</div>
                    )
                }
            </div>
            <div className="flex justify-center p-6">
                <Button className="border-2" type="submit">Buscar</Button>
            </div>
        </form>
    );
}