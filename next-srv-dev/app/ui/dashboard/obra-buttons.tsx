"use client";

import {
  habilitarObraAction,
  finalizarObraAction,
  eliminarObraAction,
} from "@/app/lib/actions/obra/actions";
import { Button } from "../button";
import { Obra } from "@/app/lib/definitions";
import { useState } from "react";

export default function ObraButtons({ obra }: { obra: Obra }) {
  const [errorButtons, setErrorButtons] = useState("");

  const handleHabilitar = async (obra: Obra) => {
    const error = await habilitarObraAction(obra);
    if (error.error) {
      setErrorButtons(error.error);
    }
  };
  const handleFinalizar = async (obra: Obra) => {
    const error = await finalizarObraAction(obra);
    if (error.error) {
      setErrorButtons(error.error);
    }
  };
  const handleEliminar = async (obra: Obra) => {
    const error = await eliminarObraAction(obra);
    if (error.error) {
      setErrorButtons(error.error);
    }
  };
  return (
    <div className="flex items-center rounded-lg bg-white-200 p-2 mb-4 border-2">
      {obra.estado === "HABILITADA" && (
        <>
          <Button
            disabled={true}
            className="cursor-not-allowed w-full text-center border-b-2"
          >
            Habilitar
          </Button>
          <Button
            onClick={() => handleFinalizar(obra)}
            className="w-full text-center border-b-2"
          >
            Finalizar
          </Button>
          <Button
            disabled={true}
            className="cursor-not-allowed w-full text-center border-b-2"
          >
            Eliminar
          </Button>
          <div>{errorButtons && <p>{errorButtons}</p>}</div>
        </>
      )}
      {obra.estado === "PENDIENTE" && (
        <div className="flex-col">
          <div className="flex">
            <Button
              onClick={() => handleHabilitar(obra)}
              className="w-full text-center border-b-2"
            >
              Habilitar
            </Button>
            <Button
              disabled={true}
              className="cursor-not-allowed w-full text-center border-b-2"
            >
              Finalizar
            </Button>
            <Button
              onClick={() => handleEliminar(obra)}
              className="w-full text-center border-b-2"
            >
              Eliminar
            </Button>
          </div>
          <div>{errorButtons && <p>{errorButtons}</p>}</div>
        </div>
      )}
      {obra.estado === "FINALIZADA" && (
        <>
          <Button
            disabled={true}
            className="cursor-not-allowed w-full text-center border-b-2"
          >
            Habilitar
          </Button>
          <Button
            disabled={true}
            className="cursor-not-allowed w-full text-center border-b-2"
          >
            Finalizar
          </Button>
          <Button
            onClick={() => handleEliminar(obra)}
            className="w-full text-center border-b-2"
          >
            Eliminar
          </Button>
        </>
      )}
    </div>
  );
}
