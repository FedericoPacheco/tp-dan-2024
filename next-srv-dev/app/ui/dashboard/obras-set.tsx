"use client";
import { Obra } from "@/app/lib/definitions";
import { lusitana } from "@/app/ui/fonts/fonts";
import { useState } from "react";
import ObraTable from "./obra-type-table";

export default function ObrasSet({
  obrasHabilitadas,
  obrasPendientes,
  obrasFinalizadas,
}: {
  obrasHabilitadas: Obra[];
  obrasPendientes: Obra[];
  obrasFinalizadas: Obra[];
}) {
  const [isActiveSummaryOpen, setIsActiveSummaryOpen] = useState(false);
  const [isPendingSummaryOpen, setIsPendingSummaryOpen] = useState(false);
  const [isCompletedSummaryOpen, setIsCompletedSummaryOpen] = useState(false);

  return (
    <main>
      {/* Resumen de obras activas */}
      <div className="flex flex-col items-center rounded-lg bg-white-200 p-2 mb-4 border-2">
        <button
          className="w-full text-left border-b-2"
          onClick={() => setIsActiveSummaryOpen(!isActiveSummaryOpen)}
        >
          <h1 className={`${lusitana.className} text-[22px]`}>
            Resumen de obras activas
          </h1>
        </button>
        {isActiveSummaryOpen && (
          <ObraTable obras={obrasHabilitadas}/>
        )}
      </div>

      {/* Resumen de obras pendientes */}
      <div className="flex flex-col items-center rounded-lg bg-white-200 p-2 mb-4 border-2">
        <button
          className="w-full text-left border-b-2"
          onClick={() => setIsPendingSummaryOpen(!isPendingSummaryOpen)}
        >
          <h1 className={`${lusitana.className} text-[22px]`}>
            Resumen de obras pendientes
          </h1>
        </button>
        {isPendingSummaryOpen && (
          <ObraTable obras={obrasPendientes}/>
        )}
      </div>

      {/* Resumen de obras finalizadas */}
      <div className="flex flex-col items-center rounded-lg bg-white-200 p-2 mb-4 border-2">
        <button
          className="w-full text-left border-b-2"
          onClick={() => setIsCompletedSummaryOpen(!isCompletedSummaryOpen)}
        >
          <h1 className={`${lusitana.className} text-[22px]`}>
            Resumen de obras finalizadas
          </h1>
        </button>
        {isCompletedSummaryOpen && (
          <ObraTable obras={obrasFinalizadas}/>
        )}
      </div>
    </main>
  );
}
