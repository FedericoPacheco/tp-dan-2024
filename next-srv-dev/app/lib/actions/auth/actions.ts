/**
 * Podria partir este archivo en auth/actions.ts y cliente/actions.ts para diferenciar una cosa de la otra,
 * luego voy a tener otras  acciones relacionadas con los clientes, que ameritan tener su propio archivo.
 */

'use server';

import { db } from '@vercel/postgres';
import bcrypt from 'bcrypt';

import { z } from 'zod';
import { sql } from '@vercel/postgres';
import { revalidatePath } from 'next/cache';
import { redirect } from 'next/navigation';

import { signIn } from '@/auth';
import { AuthError } from 'next-auth';

import { Cliente,ClientResponse,AuthUserResponse } from '../../definitions';

export type State = {
  errors?: {
    name?: string[];
    cuit?: string[];
    email?: string[];
    password?: string[];
  };
  message?: string | null;
};

const FormSchema = z.object({
  id: z.string(),
  name: z.string({
    invalid_type_error: 'Por favor escriba un nombre.',
  }).min(2, { message: 'El nombre debe tener al menos 2 caracteres.' })
    .max(50, { message: 'El nombre no debe tener más de 50 caracteres.' }),
  cuit: z.string({
    invalid_type_error: 'Por favor escriba un CUIT.',
  }).length(11, { message: 'El CUIT debe tener exactamente 11 caracteres.' })
    .regex(/^\d+$/, { message: 'El CUIT debe contener solo números.' }),
  email: z.string({
    invalid_type_error: 'Por favor escriba un email.',
  }).email({ message: 'Por favor escriba un email válido.' }),
  password: z.string({
    invalid_type_error: 'Por favor escriba una contraseña.',
  }).min(6, { message: 'La contraseña debe tener al menos 6 caracteres.' }),
});
const CreateClient = FormSchema.omit({ id: true });

const urlGeneral = process.env.NEXT_PUBLIC_GW_ADDR;

export async function createClient(prevState: State,formData: FormData) {
  const validatedFields = CreateClient.safeParse({
    name: formData.get("name"),
    cuit: formData.get("cuit"),
    email: formData.get("email"),
    password: formData.get("password"),
  });
  if (!validatedFields.success) {
    return {
      errors: validatedFields.error.flatten().fieldErrors,
      message: "Error con algunos campos, no se pudo crear cliente.",
    };
  }
  const { name, cuit, email, password } = validatedFields.data;

  if (
    (
      await fetch(`${urlGeneral}/clientes/api/clientes/searchByEmail?email=${email}`)
    ).ok ||
    (
      await fetch(
        urlGeneral +
          "/clientes/api/usuarios-habilitados/searchByEmail?email=" +
          email
      )
    ).ok
  )
    return {
      errors: { email: ["El email ya se encuentra registrado."] },
      message: "Error con algunos campos, no se pudo crear cliente.",
    };
  if (
    (
      await fetch(
        urlGeneral + "/clientes/api/clientes/searchByCuit?cuit=" + cuit
      )
    ).ok
  )
    return {
      errors: { cuit: ["El cuit ya se encuentra registrado."] },
      message: "Missing Fields. Failed to Create Invoice.",
    };

  //Crear Cliente
  const newClienteResponse: ClientResponse = await fetch(
    urlGeneral + "/clientes/api/clientes",
    {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        nombre: name,
        cuit: cuit,
        correoElectronico: email,
        maximoDescubierto: "1000",
        maximaCantidadObrasEnEjecucion: "2",
        descubierto: "0",
      }),
    }
  );
  
  //TODO - Verificar esta validacion, no se si funcionara.
  if (!newClienteResponse.ok)
    return { message: "Error de Base de datos: No se pude crear el cliente." };
  const newClient : Cliente = await newClienteResponse.json();
  
  //Crear Usuario autorizado por defecto
  //NOTA : le pase el objeto newClient en vez del id, podria ser un problema en el futuro.
  const newAuthUserResponse : AuthUserResponse = await fetch(
    urlGeneral + "/clientes/api/usuarios-habilitados",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        nombre: name,
        apellido: "default user",
        dni: "00000000",
        correoElectronico: email,
        cliente: newClient,
      }),
    }
  );
  const authUser = await newAuthUserResponse.json();
  //Colocarlo en la DB de auth
  const hashedPassword = await bcrypt.hash(password, 10);
  const client = await db.connect();
  try {
      await client.sql`
      INSERT INTO users (name, email, password)
      VALUES (${authUser.nombre}, ${authUser.correoElectronico}, ${hashedPassword})
      ON CONFLICT (email) DO NOTHING;
      `;
      } catch (error) {
        return {
          message: 'Database Error: Failed to Create Invoice.',
        };
    }

  revalidatePath("/login");
  redirect("/login");
}
export async function authenticate(
    prevState: string | undefined,
    formData: FormData,
  ) {
    try {
      await signIn('credentials', formData);
    } catch (error) {
      if (error instanceof AuthError) {
        switch (error.type) {
          case 'CredentialsSignin':
            return 'Invalid credentials.';
          default:
            return 'Something went wrong.';
        }
      }
      throw error;
    }
  }