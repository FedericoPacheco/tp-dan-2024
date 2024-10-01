"use client";

import { Obra } from "@/app/lib/definitions";
import { lusitana } from "../fonts/fonts";
import ObraButtons from "./obra-buttons";

export default function ObraTable({ obras }: { obras: Obra[] }) {
  return (
    <div className="w-full mt-2">
      {obras.map((obra) => (
        <div
          key={obra.id}
          className="flex flex-col items-center rounded-lg bg-white-200 p-2 mb-4 border-2"
        >
          <div>
            <h1 className={`${lusitana.className} text-[22px]`}>
              {obra.direccion}
            </h1>
            <p>{`Presupuesto: $${obra.presupuesto}`}</p>
            <p>{`Estado: ${obra.estado}`}</p>
            <p>{`Cliente: ${obra.cliente.nombre} ${obra.cliente.cuit}`}</p>
          </div>
          <div>
            <ObraButtons obra = {obra}/>
          </div>
        </div>
      ))}
    </div>
  );
}
