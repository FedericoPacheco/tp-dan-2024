"use client";
import { Producto } from "@/app/lib/definitions";
import { deleteProduct } from "@/app/lib/actions/producto/actions";
import Image from "next/image";
import { Button } from "../button";
import Link from "next/link";
import { PencilIcon, TrashIcon } from "@heroicons/react/24/outline";

export default function ProductsTable({
  products,
  isAdmin,
}: {
  products: Producto[];
  isAdmin: boolean;
}) {
  const emptyProductsList = [];
  products.map((product) => emptyProductsList.push(product));

  if (emptyProductsList.length <= 5)
    for (let i = 0; i < 6; i++)
      emptyProductsList.push({
        id: " ",
        nombre: " - ",
        descripcion: " - ",
        precio: " - ",
        descuentoPromocional: " - ",
        stockActual: " - ",
        stockMinimo: " - ",
        categoria: {
          id: i,
          nombre: " ",
        },
      });

  const handleDelete = (id: string) => {
    console.log("Deleting product with id: ", id);
    deleteProduct(String(id));
  };

  return (
    <div className="mt-6 p-2 border-2 overflow-y-auto md:h-1/2 xl:h-5/6">
      <table className="min-w-full text-gray-900 table h-full">
        <thead className="rounded-lg text-center text-sm font-normal">
          <tr>
            <th scope="col" className="font-medium p-2 border-2">
              Categoria
            </th>
            <th scope="col" className="font-medium p-2 border-2">
              Producto
            </th>
            <th
              scope="col"
              className="hidden md:table-cell font-medium p-2 border-2"
            >
              Descripción
            </th>
            <th scope="col" className="font-medium p-2 border-2">
              Precio
            </th>
            <th scope="col" className="font-medium p-2 border-2">
              <p className="hidden md:table-cell">Desc. Promocional</p>
              <p className="sm:table-cell md:hidden">%</p>
            </th>
            <th scope="col" className="font-medium p-2 border-2">
              Stock
            </th>
            <th
              scope="col"
              className="hidden md:table-cell font-medium p-2 border-2"
            >
              Stock minimo
            </th>
            <th scope="col" className="font-medium p-2 border-2">
              Acciones
            </th>
          </tr>
        </thead>
        <tbody className="bg-white border-2">
          {emptyProductsList?.map((product) => (
            <tr
              key={product.id}
              className="w-full border-b py-3 text-sm last-of-type:border-none [&:first-child>td:first-child]:rounded-tl-lg [&:first-child>td:last-child]:rounded-tr-lg [&:last-child>td:first-child]:rounded-bl-lg [&:last-child>td:last-child]:rounded-br-lg"
            >
              <td className="text-center whitespace-nowrap px-3 py-3 border-2">
                <div className="flex">
                  <Image
                    key={product.id}
                    src={"/icons/brick.svg"}
                    className="mr-2 rounded-full"
                    width={28}
                    height={28}
                    alt={`${product.nombre}'s profile picture`}
                  />
                  <p className="hidden md:table-cell">
                    {product.categoria.nombre}
                  </p>
                </div>
              </td>
              <td className="text-center py-3 pl-6 pr-3 border-2">
                {product.nombre}
              </td>
              <td className="text-center hidden md:table-cell px-3 py-3 border-2 ">
                {product.descripcion}
              </td>
              <td className="text-center whitespace-nowrap px-3 py-3 border-2">
                {product.precio}
              </td>
              <td className="text-center whitespace-nowrap px-3 py-3 border-2">
                {`${product.descuentoPromocional}%`}
              </td>
              <td className="text-center whitespace-nowrap px-3 py-3 border-2">
                {product.stockActual}
              </td>
              <td className="text-center hidden md:table-cell whitespace-nowrap px-3 py-3 border-2">
                {product.stockMinimo}
              </td>
              <td className="text-center hidden md:table-cell whitespace-nowrap px-3 py-3 border-2">
                <div>
                  {isAdmin && (
                    <>
                      <Link href={`/dashboard/productos/${product.id}/edit`}>
                        <Button>
                          <PencilIcon className="ml-auto h-5 w-5" />
                        </Button>
                      </Link>
                      <Button
                        onClick={() => {
                          handleDelete(product.id);
                        }}
                      >
                        <TrashIcon className="ml-auto h-5 w-5" />
                      </Button>
                    </>
                  )}
                  {!isAdmin && (
                    <>
                      <Link href={`/dashboard/productos/${product.id}/edit`}>
                        <Button disabled={true} className="cursor-not-allowed">
                          <PencilIcon className="ml-auto h-5 w-5" />
                        </Button>
                      </Link>
                      <Button
                        disabled={true}
                        className="cursor-not-allowed"
                        onClick={() => {
                          handleDelete(product.id);
                        }}
                      >
                        <TrashIcon className="ml-auto h-5 w-5" />
                      </Button>
                    </>
                  )}
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
