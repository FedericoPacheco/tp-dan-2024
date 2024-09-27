'use server';

import { z } from 'zod';
import { revalidatePath } from "next/cache";
import { AuthUserResponse, Obra, ObraResponse, UsuarioHabilitado } from "../../definitions";
import { auth } from "@/auth";
import { fetchClienteById } from '../../data/cliente/data';
import { redirect } from 'next/navigation';

const urlGeneral = process.env.NEXT_PUBLIC_GW_ADDR;

export async function habilitarObraAction(obra: Obra): Promise<{ error: string | undefined; }> {
    const session = await auth();

    const url_user = `${urlGeneral}/clientes/api/usuarios-habilitados/searchByEmail?email=${session?.user?.email}`;
    const clientResponse: AuthUserResponse = await fetch(url_user);
    if (!clientResponse.ok) {
        throw new Error("Error, no se pudo obtener el usuario");
    }
    const user: UsuarioHabilitado = await clientResponse.json();

    const url = `${urlGeneral}/clientes/api/obras/${obra.id}/estado/${'HABILITADA'}/usuario/${user.id}`;
    const obraResponse: ObraResponse = await fetch(url, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
    });
    if (!obraResponse.ok) {
        return { error: "Error, cliente no cumple las condiciones" };
    }
    revalidatePath('/dashboard');
    return { error: undefined };
}
export async function finalizarObraAction(obra: Obra) {
    const session = await auth();

    const url_user = `${urlGeneral}/clientes/api/usuarios-habilitados/searchByEmail?email=${session?.user?.email}`;
    const clientResponse: AuthUserResponse = await fetch(url_user);
    if (!clientResponse.ok) {
        throw new Error("Error, no se pudo obtener el usuario");
    }
    const user: UsuarioHabilitado = await clientResponse.json();

    const url = `${urlGeneral}/clientes/api/obras/${obra.id}/estado/${'FINALIZADA'}/usuario/${user.id}`;
    const obraResponse: ObraResponse = await fetch(url, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
    });
    if (!obraResponse.ok) {
        return { error: "Error, no se pudo finalizar la obra" };
    }
    revalidatePath('/dashboard');
    return { error: undefined };
}
export async function eliminarObraAction(obra: Obra) {
    const url = `${urlGeneral}/clientes/api/obras/${obra.id}`;

    const deleteObraRespnse: ObraResponse = await fetch(
        url,
        {
            method: "DELETE",
        }
    );
    if (!deleteObraRespnse.ok) {
        return { error: "Error, no se pudo eliminar la obra" };
    }
    revalidatePath('/dashboard');
    return { error: undefined };
}
export type State = {
    errors?: {
        direccion?: string[];
        latitud?: string[];
        longitud?: string[];
        presupuesto?: string[];
        estado?: string[];
        cliente?: string[];
    } | {};
    message?: string | null;
} | undefined;

const ObraSchema = z.object({
    id: z.string(),
    direccion: z.string(),
    latitud: z.string(),
    longitud: z.string(),
    presupuesto: z.string(),
    estado: z.string(),
    clienteId: z.string().min(1, { message: "Seleccione un cliente" }),
});

const ObraSchemaCreate = ObraSchema.omit({ id: true , estado: true });


export async function createObra(prevState: State, formData: FormData) {
    const validData = ObraSchemaCreate.safeParse({
        direccion: formData.get("direccion"),
        latitud: formData.get("latitud"),
        longitud: formData.get("longitud"),
        presupuesto: formData.get("presupuesto"),
        clienteId : formData.get("cliente"),
    });
    if (!validData.success) {
        return {
            errors: validData.error.flatten().fieldErrors,
            message: "Error, se ingreso valor invalido en algun campo",
        };
    }
    const {direccion, latitud, longitud, presupuesto, clienteId} = validData.data;
    const estado = 'PENDIENTE';
    const cliente = await fetchClienteById(Number(clienteId));

    const url = `${urlGeneral}/clientes/api/obras`;
    const response = await fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            direccion : direccion, 
            latitud : latitud,
            longitud : longitud,
            presupuesto : presupuesto,
            estado : estado,
            cliente : cliente}),
    });
    if (!response.ok) {
        return {
            errors: {},
            message: "Error, no se pudo crear la obra",
        };
    }
    revalidatePath('/dashboard');
    redirect('/dashboard');
}