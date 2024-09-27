import { fetchProducts } from "@/app/lib/data";
import ForbbidenAccess from "@/app/ui/forbbiden";
import AñadirStock from "@/app/ui/productos/edit-stock-form";
import { lusitana } from "@/app/ui/fonts/fonts";
import { auth } from "@/auth";

export default async function Page() {
  const session = await auth();
  //Funciona, pero esta muy atado con alambre.
  const isAdmin = session?.user?.email === "admin@admin.com";

  if (!isAdmin) {
    return <ForbbidenAccess />;
  }
  const productos = await fetchProducts({ params: {}, searchParams: {} });

  return (
    <div className="bg-white-200">
      <h1
        className={`${lusitana.className} text-center text-[22px] text-black-50 py-2`}
      >
        <strong>Reponer Stock</strong>
      </h1>
      <AñadirStock productos={productos} />
    </div>
  );
}
