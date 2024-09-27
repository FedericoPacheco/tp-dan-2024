"use client";

import { Session } from "next-auth";
import { lusitana } from "../fonts/fonts";

export default function UserInfo({
  session,
  isDefaultUser,
  isAdmin,
}: {
  session: Session | null;
  isDefaultUser: boolean;
  isAdmin: boolean;
}) {
  return (
    <div>
      {!isDefaultUser && !isAdmin && (
        <>
          <p className={`${lusitana.className} p-2`}>
            {`Usuario: ${session?.user?.name}`}
          </p>
          <p className={`${lusitana.className} p-2`}>
            {`Privilegios: minimos`}
          </p>
        </>
      )}
      {isDefaultUser && !isAdmin && (
        <>
          <p className={`${lusitana.className} p-2`}>
            {`Usuario: ${session?.user?.name}`}
          </p>
          <p className={`${lusitana.className} p-2`}>
            {`Privilegios: default user`}
          </p>
        </>
      )}
      {isAdmin && (
        <>
          <p className={`${lusitana.className} p-2`}>
            {`Usuario: ${session?.user?.name}`}
          </p>
          <p className={`${lusitana.className} p-2`}>
            {`Privilegios: administrador`}
          </p>
        </>
      )}
    </div>
  );
}
