import { AuthUserResponse, AuthUsersResponse, Cliente, ClientResponse, ClientsResponse, Obra, ObraSegmentada, ObrasResponse, UsuarioHabilitado } from '../../definitions';

const urlGeneral = process.env.NEXT_PUBLIC_GW_ADDR;

export async function fetchClientes() : Promise<Cliente[]> {
  try {
    const url = `${urlGeneral}/clientes/api/clientes`;
    const response : ClientsResponse = await fetch(url);
    if(!response.ok) throw new Error('Failed to fetch clientes data.');
    
    const clientes : Cliente[] = await response.json()
    
    return clientes;
  } catch (error) {
    console.error('API Error:', error);
    throw new Error('Failed to fetch clientes data.');
  }  
}
export async function fetchClienteById(id : number){
  const url = `${urlGeneral}/clientes/api/clientes/${id}`
  const clientResponse : ClientResponse = await fetch(url);
  if(!clientResponse.ok) throw new Error('Failed to fetch cliente.');
  const cliente : Cliente = await clientResponse.json();
  return cliente;
}
export async function fetchClienteByEmail(mail : string|undefined|null) : Promise<Cliente> {
  if(!mail) throw new Error('Mail not found.');
  const url = `${urlGeneral}/clientes/api/clientes/searchByEmail?email=${mail}`
  const clientResponse : ClientResponse = await fetch(url);
  if(!clientResponse.ok) throw new Error('Failed to fetch cliente.');
  const cliente : Cliente = await clientResponse.json();
  return cliente;
}
export async function fetchUserByEmail(mail : string|undefined|null) : Promise<UsuarioHabilitado> {
  if(!mail) throw new Error('Mail not found.');
  const url = `${urlGeneral}/clientes/api/usuarios-habilitados/searchByEmail?email=${mail}`
  const userResponse : AuthUserResponse = await fetch(url);
  if(!userResponse.ok) throw new Error('Failed to fetch user.');
  const user : UsuarioHabilitado = await userResponse.json();
  return user;
}
export async function fetchUsers(cliente:Cliente|null) : Promise<UsuarioHabilitado[]> {
  if(!cliente) throw new Error('Cliente not found.');
  const url = `${urlGeneral}/clientes/api/usuarios-habilitados/searchByCliente?idCliente=${cliente.id}`
  const usersResponse : AuthUsersResponse = await fetch(url);
  if(!usersResponse.ok) throw new Error('Failed to fetch users.');
  const users : UsuarioHabilitado[] = await usersResponse.json();
  return users;
}
export async function fetchObras() : Promise<ObraSegmentada> {
  try {
    const url = urlGeneral + "/clientes/api/obras";
    const response : ObrasResponse = await fetch(url);
    if(!response.ok) throw new Error('Failed to fetch obras data.');
    
    const obras = await response.json()

    const obrasHabilitadas : Obra[] = obras.filter((obra) => obra.estado === 'HABILITADA');
    
    const obrasPendientes : Obra[] = obras.filter((obra) => obra.estado === 'PENDIENTE');
    
    const obrasFinalizadas : Obra[] = obras.filter((obra) => obra.estado === 'FINALIZADA');

    const obrasSegmentadas : ObraSegmentada = {obrasHabilitadas,obrasPendientes,obrasFinalizadas};

    return obrasSegmentadas;
    
  } catch (error) {
    console.error('API Error:', error);
    throw new Error('Failed to fetch obras data.');
  }  
}
export async function fetchObrasCliente(mail : string) : Promise<ObraSegmentada> {

  const clientResponse : ClientResponse = await fetch(urlGeneral + "/clientes/api/clientes/searchByEmail?email=" + mail);

  if(!clientResponse.ok) throw new Error('Failed to fetch cliente.');

  const cliente : Cliente = await clientResponse.json();

  try {
    const url = urlGeneral + "/clientes/api/obras";
    const response : ObrasResponse = await fetch(url);
    if(!response.ok) throw new Error('Failed to fetch obras data.');
    
    const obrasNonFiltered = await response.json()

    const obras : Obra[] = obrasNonFiltered.filter((obra) => obra.cliente.id === cliente.id);

    const obrasHabilitadas : Obra[] = obras.filter((obra) => obra.estado === 'HABILITADA');
    
    const obrasPendientes : Obra[] = obras.filter((obra) => obra.estado === 'PENDIENTE');
    
    const obrasFinalizadas : Obra[] = obras.filter((obra) => obra.estado === 'FINALIZADA');

    const obrasSegmentadas : ObraSegmentada = {obrasHabilitadas,obrasPendientes,obrasFinalizadas};

    return obrasSegmentadas;
    
  } catch (error) {
    console.error('API Error:', error);
    throw new Error('Failed to fetch obras data.');
  }  
}