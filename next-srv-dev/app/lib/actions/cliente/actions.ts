export type StateClient = {
    message: string | null;
    errors: Record<string, string>;
};
export async function editCliente(prevState: StateClient, formData: FormData) {
    console.log(prevState);
    console.log(formData);
    return {message: 'Cliente editado', errors: {}};
}
export type StateUser = {
    message: string | null;
    errors: Record<string, string>;
};

export async function editUsuario(prevState: StateUser, formData: FormData) {
    console.log(prevState);
    console.log(formData);
    return {message: 'Usuario editado', errors: {}};
}
