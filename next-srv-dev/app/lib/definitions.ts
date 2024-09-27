import { list } from "postcss";

export type User = {
  id: string;
  name: string;
  email: string;
  password: string;
};

export type Cliente = {
  id: string;
  nombre: string;
  correoElectronico: string;
  cuit: string;
  maximoDescubierto: string;
  maximaCantidadObrasEnEjecucion: string;
  descubierto: "0"
};
export type UsuarioHabilitado = {
  id: string;
  nombre: string;
  apellido: string;
  dni: string;
  correoElectronico: string;
  cliente: Cliente;
}
export type Obra = {
  id: number;
  direccion: string;
  latitud: number;
  longitud: number;
  presupuesto: number;
  estado: ObraState;
  cliente: Cliente;
};
export type ObraState = 'PENDIENTE' | 'HABILITADA' | 'FINALIZADA';

export type ObraSegmentada = {
  obrasHabilitadas : Obra[];
  obrasPendientes : Obra[];
  obrasFinalizadas : Obra[];
};
export type Categoria = {
  id: number;
  nombre: string;
  productos: Producto[];
};
export type Producto = {
  id: number;
  nombre: string;
  descripcion: string;
  stockActual: number;
  stockMinimo: number;
  precio: number;
  descuentoPromocional: number;
  categoria: Categoria;
};

export interface ClientResponse extends Response {
  json(): Promise<Cliente>;
}
export interface ClientsResponse extends Response {
  json(): Promise<Cliente[]>;
}
export interface AuthUserResponse extends Response {
  json(): Promise<UsuarioHabilitado>;
}
export interface AuthUsersResponse extends Response {
  json(): Promise<UsuarioHabilitado[]>;
}
export interface CategoriesResponse extends Response {
  json(): Promise<Categoria[]>;
}
export interface CategoryResponse extends Response {
  json(): Promise<Categoria>;
}
export interface ProductResponse extends Response {
  json(): Promise<Producto>;
}
export interface ObraResponse extends Response {
  json(): Promise<Obra[]>;
}
export interface ObrasResponse extends Response {
  json(): Promise<Obra[]>;
}
export type ProductSearchParametersContainer = {
  params: {},
  searchParams: {
    categoria?: string;
    nombre?: string;
    codigo?: string;
    precioMin?: string;
    precioMax?: string;
    stockMin?: string;
    stockMax?: string;
  }
}