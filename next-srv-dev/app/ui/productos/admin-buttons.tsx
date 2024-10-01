import Link from "next/link";
import { Button } from "../button";

export default function AdminButtons() {
  return (
    <div className="flex items-center justify-center p-2">
      <div className="rounded-lg border-2">
        <Link href={"/dashboard/productos/stock"}>
            <Button>Reponer Stock</Button>
        </Link>
      </div>
      <div className="rounded-lg border-2">
        <Link href={"/dashboard/productos/create"}>
            <Button>Crear Producto</Button>
        </Link>
      </div>
    </div>
  );
}
