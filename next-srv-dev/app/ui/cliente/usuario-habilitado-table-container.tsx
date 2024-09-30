"use client";

import { UsuarioHabilitado } from "@/app/lib/definitions";
import { useState } from "react";
import { UsersTable } from "./users-table";
import { ArrowDownIcon } from "@heroicons/react/24/outline";

export default function UserTableContainer({
  usuarios,
}: {
  usuarios: UsuarioHabilitado[];
}) {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <div>
      <div className="flex flex-col items-center rounded-lg bg-white-200 mb-4 border-2 border-white-800">
        <button className="w-full text-left" onClick={() => setIsOpen(!isOpen)}>
          <div className="flex justify-center w-full p-4">
            <ArrowDownIcon className="h-5 w-5 mr-auto" />
            <h1 className="w-auto">Usuarios habilitados</h1>
            <ArrowDownIcon className="h-5 w-5 ml-auto" />
          </div>
        </button>
        <div className="w-full">{isOpen && <UsersTable usuarios={usuarios} />}</div>
      </div>
    </div>
  );
}
