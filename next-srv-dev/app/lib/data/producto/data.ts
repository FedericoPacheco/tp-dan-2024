import {
  Categoria,
  CategoriesResponse,
  CategoryResponse,
  ProductSearchParametersContainer,
  Producto,
} from '../../definitions';

const urlGeneral = process.env.NEXT_PUBLIC_GW_ADDR;

export async function fetchCategories() : Promise<Categoria[]> {
  try {
    const url = urlGeneral + "/productos/api/categorias";
    const response : CategoriesResponse = await fetch(url);
    if(!response.ok) throw new Error('Failed to fetch category data.');
    return await response.json();
  } catch (error) {
    console.error('API Error:', error);
    throw new Error('Failed to fetch category data.');
  }  
}
export async function fetchCategory(id: number) : Promise<Categoria> {
  try {
    const url = urlGeneral + `/productos/api/categorias/${id}`;
    const response : CategoryResponse = await fetch(url);
    if(!response.ok) throw new Error('Failed to fetch category data.');
    return await response.json();
  } catch (error) {
    console.error('API Error:', error);
    throw new Error('Failed to fetch category data.');
  }  
}
export async function fetchProducts(searchParamsContainer : ProductSearchParametersContainer){
  const url = urlGeneral + "/productos/api/productos";
  const response = await fetch(url);
  let data : Producto[];
  if(response.ok) data = await response.json();
  else throw new Error('Failed to fetch product data.');
  //Voy a escribir unos filtros bastante feos, lo ideal seria hacer esto en el microsrervicio.
  const searchParams = searchParamsContainer.searchParams;

  if(searchParams.categoria) data = data.filter((product) => product.categoria.id === Number(searchParams.categoria));
  
  if(searchParams.nombre) data = data.filter((product) => product.nombre.includes(searchParams.nombre ?? ''));

  if(searchParams.codigo) data = data.filter((product) => product.id === Number(searchParams.codigo));

  if(searchParams.precioMin) data = data.filter((product) => product.precio >= Number(searchParams.precioMin));
  
  if(searchParams.precioMax) data = data.filter((product) => product.precio <= Number(searchParams.precioMax));
  
  if(searchParams.stockMin) data = data.filter((product) => product.stockActual >= Number(searchParams.stockMin));
  
  if(searchParams.stockMax) data = data.filter((product) => product.stockActual <= Number(searchParams.precioMax));
  
  return data;
}
export async function fetchProduct(id: number) {
  const url = urlGeneral + `/productos/api/productos/${id}`;
  const response = await fetch(url);
  if(response.ok) return await response.json();
  else throw new Error('Failed to fetch product data.');
}