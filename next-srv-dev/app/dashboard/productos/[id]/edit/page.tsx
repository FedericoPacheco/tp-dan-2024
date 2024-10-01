import { fetchProduct } from "@/app/lib/data/producto/data";
import { lusitana } from "@/app/ui/fonts/fonts";
import ForbbidenAccess from "@/app/ui/forbbiden";
import EditStock from "@/app/ui/productos/edit-form";
import { auth } from "@/auth";

type SegmentParams = {
  id: string;
};

export default async function Page({ params }: { params: Promise<SegmentParams> }) {
  const session = await auth();
  //Funciona, pero esta muy atado con alambre.
  const isAdmin = session?.user?.email === "admin@admin.com";
  const paramsReales = await params;
  const id = Number(paramsReales.id);
  if (!isAdmin) {
    return <ForbbidenAccess />;
  }

  const producto = await fetchProduct(Number(id));

  return (
    <div>
      <h1
        className={`${lusitana.className} text-center text-[22px] text-black-50 py-2`}
      >
        <strong>Edicion de producto existente</strong>
      </h1>
      <EditStock producto={producto}/>
    </div>
  );
}
