import { fetchCategories } from "@/app/lib/data/producto/data";
import { lusitana } from "@/app/ui/fonts/fonts";
import ForbbidenAccess from "@/app/ui/forbbiden";
import CreateProductForm from "@/app/ui/productos/create-form";
import { auth } from "@/auth";

export default async function Page() {
  const session = await auth();
  //Funciona, pero esta muy atado con alambre.
  const isAdmin = session?.user?.email === "admin@admin.com";

  if (!isAdmin) {
    return(<ForbbidenAccess />);  
  }
  
  const categories = await (fetchCategories());
  return (
    <main className="flex flex-col items-center justify-center bg-white-100">
      <h1
        className={`${lusitana.className} text-center text-[22px] text-black-50 py-2`}
      >
        <strong>Nuevo Producto</strong>
      </h1>
      <CreateProductForm
        categories={categories}
      />
    </main>
  );
}
