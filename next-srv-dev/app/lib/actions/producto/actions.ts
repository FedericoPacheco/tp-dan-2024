'use server';

import { z } from 'zod';
import { revalidatePath } from 'next/cache';
import { redirect } from 'next/navigation';
import { ProductResponse, Categoria, Producto } from '../../definitions';
import { fetchCategory, fetchProduct } from '../../data/producto/data';

//Este state estaria aplicado a los filtros, no a entidades.
export type StateFilter = {
  errors?: {
    categoria?: string[];
    nombre?: string[];
    codigo?: string[];
    precioMin?: string[];
    precioMax?: string[];
    stockMin?: string[];
    stockMax?: string[];
  } | {};
  message?: string | null;
};
export type StateCreate = {
  errors?: {
    categoria?: string[];
    nombre?: string[];
    descripcion?: string[];
    precio?: string[];
    descuento?: string[];
    stock?: string[];
    stockMinimo?: string[];
  } | {};
  message?: string | null;
} | undefined;

export type StateEditStock = {
  errors?: {
    producto?: string[];
    stock?: string[];
  } | {};
  message?: string | null;
} | undefined;

const FormSchemaFilters = z.object({
  categoria: z.string().optional(),
  nombre: z.string().optional(),
  codigo: z.string().optional(),
  precioMin: z.string().optional(),
  precioMax: z.string().optional(),
  stockMin: z.string().optional(),
  stockMax: z.string().optional(),
});

const FormSchemaCreate = z.object({
  categoriaId: z.string().min(1, { message: "Debe seleccionar una categoria" }),
  nombre: z.string().min(1, { message: "Debe ingresar un nombre" }),
  descripcion: z.string().min(15, { message: "Debe ingresar una descripcion de al menos 15 caracteres" }),
  precio: z.string().min(1, { message: "Debe ingresar un precio" }),
  descuento: z.string().min(1, { message: "Debe ingresar un descuento" }),
  stock: z.string().min(1, { message: "Debe ingresar un stock" }),
  stockMinimo: z.string().min(1, { message: "Debe ingresar un stock minimo" }),
});
const FormSchemaEdit = z.object({
  productoId : z.string().min(1, { message: "Debe seleccionar un producto" }),
  nombre: z.string().min(1, { message: "Debe ingresar un nombre" }),
  descripcion: z.string().min(15, { message: "Debe ingresar una descripcion de al menos 15 caracteres" }),
  precio: z.string().min(1, { message: "Debe ingresar un precio" }),
  descuento: z.string().min(1, { message: "Debe ingresar un descuento" }),
});

const FormSchemaEditStock = z.object({
  productoId: z.string().min(1, { message: "Debe seleccionar un producto" }),
  stock: z.string().min(1, { message: "Debe ingresar un stock" }),
});

const urlGeneral = process.env.NEXT_PUBLIC_GW_ADDR;

export async function searchByFilters(prevState: StateFilter, formData: FormData) {

  const validatedFields = FormSchemaFilters.safeParse({
    //Esto seria semanticamente incorrecto, esto trae el id de la categoria, no la entidad.
    categoria: formData.get("categoria"),
    nombre: formData.get("nombre"),
    codigo: formData.get("codigo"),
    precioMin: formData.get("precioMin"),
    precioMax: formData.get("precioMax"),
    stockMin: formData.get("stockMin"),
    stockMax: formData.get("stockMax"),
  });

  if (!validatedFields.success) {
    return {
      errors: validatedFields.error.flatten().fieldErrors,
      message: "Error, se ingreso valor invalido en algun campo",
    };
  }
  const { categoria, nombre, codigo, precioMin, precioMax, stockMin, stockMax } = validatedFields.data;
  revalidatePath("/dashboard/productos");
  redirect("/dashboard/productos?categoria=" + categoria + "&nombre=" + nombre + "&codigo=" + codigo + "&precioMin=" + precioMin + "&precioMax=" + precioMax + "&stockMin=" + stockMin + "&stockMax=" + stockMax);
}
export async function createProduct(prevState: StateCreate, formData: FormData) {
  const validatedFields = FormSchemaCreate.safeParse({
    categoriaId: formData.get("categoria"),
    nombre: formData.get("nombre"),
    descripcion: formData.get("descripcion"),
    precio: formData.get("precio"),
    descuento: formData.get("descuentoPromocional"),
    stock: formData.get("stockActual"),
    stockMinimo: formData.get("stockMinimo"),
  });

  if (!validatedFields.success) {
    return {
      errors: validatedFields.error.flatten().fieldErrors,
      message: "Error, se ingreso valor invalido en algun campo",
    };
  }

  const { categoriaId, nombre, descripcion, precio, descuento, stock, stockMinimo } = validatedFields.data;

  const categoria: Categoria = await fetchCategory(Number(categoriaId));

  const newProductResponse: ProductResponse = await fetch(
    urlGeneral + "/productos/api/productos",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        //No le tengo tanta fe a dejar categoria asi definido.
        categoria: categoria,
        nombre: nombre,
        descripcion: descripcion,
        precio: precio,
        descuentoPromocional: descuento,
        stockActual: stock,
        stockMinimo: stockMinimo,
      }),
    }
  );
  console.log(newProductResponse);
  if (!newProductResponse.ok) {
    return {
      errors: {},
      message: "Error, no se pudo crear el producto",
    }
  };
  revalidatePath("/dashboard/productos");
  redirect("/dashboard/productos?categoria1=" + categoriaId + "&nombre=" + nombre + "&codigo=" + "&precioMin=" + "&precioMax=" + "&stockMin=" + "&stockMax=");
}

export async function editStock(prevState: StateEditStock, formData: FormData) {
  const validatedFields = FormSchemaEditStock.safeParse({
    productoId: formData.get("producto"),
    stock: formData.get("stock"),
  });

  if (!validatedFields.success) {
    return {
      errors: validatedFields.error.flatten().fieldErrors,
      message: "Error, se ingreso valor invalido en algun campo",
    };
  }

  const { productoId, stock } = validatedFields.data;

  const producto: Producto = await fetchProduct(Number(productoId));

  producto.stockActual = Number(producto.stockActual) + Number(stock);

  const updateProductResponse: ProductResponse = await fetch(
    urlGeneral + "/productos/api/productos",
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        id : producto.id,
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
      message: "Error, no se pudo editar stock del producto",
    }
  };
  revalidatePath("/dashboard/productos");
  redirect("/dashboard/productos");
}
export async function editProduct(prevState: StateCreate, formData: FormData) {
  const validatedFields = FormSchemaEdit.safeParse({
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
  const {productoId,nombre, descripcion, precio, descuento } = validatedFields.data;
  
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
        id : producto.id,
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
  revalidatePath("/dashboard/productos");
}
