'use server';

import { revalidatePath } from "next/cache";
import { redirect } from 'next/navigation';
import { z } from "zod";

export type State = {
    errors?: {
        observaciones?: string;
        total?: string;
        detalles?: string;
        cliente?: string;
        usuario?: string;
        obra?: string;
    } | object;
    message?: string | null;
} | undefined;

const HistorialPedidoSchema = z.object({
    estado: z.string(),
    fecha: z.string(),
    detalle: z.string(),
    userEstadoId: z.string(),
});
const DetallePedidoSchema = z.object({
    idProducto: z.string(),
    cantidad: z.number(),
});
const DetallesPedido = z.array(DetallePedidoSchema);

const PedidoSchema = z.object({
    id: z.string(),
    fecha: z.string(),
    observaciones: z.string(),
    total: z.string(),
    historialEstados: z.array(HistorialPedidoSchema),
    estado: z.string(),
    detalles: z.array(DetallePedidoSchema),
    clienteId: z.string().min(1, { message: "Seleccione un cliente" }),
    usuarioId: z.string().min(1, { message: "Seleccione un usuario" }),
    obraId: z.string().min(1, { message: "Seleccione una obra" }),
});
const PedidoSchemaCreate = PedidoSchema.omit({ id: true, fecha: true, estado: true, historialEstados: true, detalles: true });

const urlGeneral = process.env.NEXT_PUBLIC_GW_ADDR;

export async function createPedido(prevState: State, formData: FormData) {
    const validData = PedidoSchemaCreate.safeParse({
        observaciones: formData.get("observaciones"),
        total: formData.get("total"),
        clienteId: formData.get("cliente"),
        usuarioId: formData.get("usuarioHabilitado"),
        obraId: formData.get("obra"),
    });

    const parsed = JSON.parse(formData.get("prodSeleccionados") as string);
    console.log(parsed);
    const validDetalles = DetallesPedido.safeParse(parsed);
    console.log(validDetalles);

    if (!validData.success) {
        return { errors: validData.error.errors };
    }
    if (!validDetalles.success) {
        console.log(validDetalles.error.errors);
        return { errors: { detalles: "Seleccione al menos un producto" } };
    }

    const { observaciones, clienteId, usuarioId, obraId } = validData.data;
    const detalles = validDetalles.data;

    console.log(detalles);

    const url = `${urlGeneral}/pedidos/api/pedidos`;
    const response = await fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            observaciones: observaciones,
            idCliente: clienteId,
            idUsuario: usuarioId,
            idObra: obraId,
            productos: detalles,

        }),
    });
    if (!response.ok) {
        return { errors : {}, message: "Error, no se pudo crear el pedido" };
    }
    revalidatePath("/dashboard/pedidos");
    redirect("/dashboard/pedidos");
}
