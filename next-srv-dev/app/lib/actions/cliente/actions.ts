import { revalidatePath } from "next/cache";
import { redirect } from "next/navigation";

export type StateClient = {
    errors?: {
        nombre?: string[];
        cuit?: string[];
        correoElectronico?: string[];
        maximoDescubierto?: string[];
        maximaCantidadObrasEnEjecucion?: string[];
        descubierto?: string[];
    }
    message?: string | null;
};
export async function editCliente(prevState: StateClient, formData: FormData) {
    /* const validatedFields = FormSchemaEdit.safeParse({
        productoId: formData.get("producto"),
        nombre: formData.get("nombre"),
        descripcion: formData.get("descripcion"),
        precio: formData.get("precio"),
        descuento: formData.get("descuentoPromocional"),
    });
    if (!validatedFields.success) {
        return {
            errors: validatedFields.error.flatten().fieldErrors,
            message: "Error, se ingreso valor invalido en algun campo",
        };
    }
    const { productoId, nombre, descripcion, precio, descuento } = validatedFields.data;

    const producto: Producto = await fetchProduct(Number(productoId));

    producto.nombre = nombre;
    producto.descripcion = descripcion;
    producto.precio = Number(precio);
    producto.descuentoPromocional = Number(descuento);

    const updateProductResponse: ProductResponse = await fetch(
        urlGeneral + "/productos/api/productos",
        {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                id: producto.id,
                nombre: producto.nombre,
                descripcion: producto.descripcion,
                precio: producto.precio,
                descuentoPromocional: producto.descuentoPromocional,
                stockActual: producto.stockActual,
                stockMinimo: producto.stockMinimo,
                categoria: producto.categoria,
            }),
        }
    );

    if (!updateProductResponse.ok) {
        return {
            errors: {},
            message: "Error, no se pudo editar el producto",
        }
    };
    revalidatePath("/dashboard/productos");
    redirect("/dashboard/productos");
}
export async function deleteProduct(id: string) {

    const deleteProductResponse: ProductResponse = await fetch(
        urlGeneral + "/productos/api/productos/" + id,
        {
            method: "DELETE",
        }
    );

    if (!deleteProductResponse.ok) {
        return {
            errors: {},
            message: "Error, no se pudo eliminar el producto",
        }
    };
 */
    redirect("/dashboard/cliente");
}
export type StateUser = {
    errors?: {
        nombre?: string[];
        apellido?: string[];
        dni?: string[];
        correoElectronico?: string[];
    };
    message?: string | null;
};

export async function editUsuario(prevState: StateUser, formData: FormData) {
    /* const validatedFields = FormSchemaEdit.safeParse({
        productoId: formData.get("producto"),
        nombre: formData.get("nombre"),
        descripcion: formData.get("descripcion"),
        precio: formData.get("precio"),
        descuento: formData.get("descuentoPromocional"),
    });
    if (!validatedFields.success) {
        return {
            errors: validatedFields.error.flatten().fieldErrors,
            message: "Error, se ingreso valor invalido en algun campo",
        };
    }
    const { productoId, nombre, descripcion, precio, descuento } = validatedFields.data;

    const producto: Producto = await fetchProduct(Number(productoId));

    producto.nombre = nombre;
    producto.descripcion = descripcion;
    producto.precio = Number(precio);
    producto.descuentoPromocional = Number(descuento);

    const updateProductResponse: ProductResponse = await fetch(
        urlGeneral + "/productos/api/productos",
        {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                id: producto.id,
                nombre: producto.nombre,
                descripcion: producto.descripcion,
                precio: producto.precio,
                descuentoPromocional: producto.descuentoPromocional,
                stockActual: producto.stockActual,
                stockMinimo: producto.stockMinimo,
                categoria: producto.categoria,
            }),
        }
    );

    if (!updateProductResponse.ok) {
        return {
            errors: {},
            message: "Error, no se pudo editar el producto",
        }
    };
    revalidatePath("/dashboard/productos");
    redirect("/dashboard/productos");
}
export async function deleteProduct(id: string) {

    const deleteProductResponse: ProductResponse = await fetch(
        urlGeneral + "/productos/api/productos/" + id,
        {
            method: "DELETE",
        }
    );

    if (!deleteProductResponse.ok) {
        return {
            errors: {},
            message: "Error, no se pudo eliminar el producto",
        }
    };
 */
    redirect('/dashboard/cliente');
}
