import { lusitana } from "@/app/ui/fonts/fonts";
import { Metadata } from "next";
import { auth } from "auth";
import ProductViewer from "@/app/ui/productos/product-viewer";
import {
  Producto,
  ProductSearchParametersContainer,
} from "@/app/lib/definitions";
import { fetchProducts } from "@/app/lib/data/producto/data";
import ProductsTable from "../../ui/productos/table";
import AdminButtons from "@/app/ui/productos/admin-buttons";
export const metadata: Metadata = {
  title: "Productos",
};
/**
 *
 * @param searchParams
 * @returns
 * Cometi cierto error de diseño aca, el formulario puedo haber sido un client component
 * me ahorraria algunos problemnas, creo que termine usando mal la funcion de formularios
 */
export default async function Page(
  searchParams: ProductSearchParametersContainer
) {
  const session = await auth();
  //Funciona, pero esta muy atado con alambre.
  const isAdmin = session?.user?.email === "admin@admin.com";

  const products: Producto[] = await fetchProducts(searchParams);
  return (
    <main className="h-full mt-2">
      <div className="flex flex-col xl:flex-row items-center justify-center h-full">
        <div className="flex flex-col items-center justify-center mr-5">
          <div className="p-4 w-full sm:w-full">
            <ProductViewer />
          </div>
          <div>{isAdmin && <AdminButtons />}</div>
        </div>
        <div className="h-full">
          <ProductsTable products={products} isAdmin={isAdmin} />
        </div>
      </div>
    </main>
  );
}
