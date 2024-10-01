import { lusitana } from "@/app/ui/fonts/fonts";


export default function Layout({ children }: { children: React.ReactNode }) {
  return (
    <div className="flex flex-col md:overflow-hidden h-full">
      <div className="flex items-center justify-center rounded-lg bg-ebony-400 h-20 md:h-40">
        <h1
          className={`${lusitana.className} sm:text-[22px] md:text-[44px] text-white-50`}
        >
          <strong>Pedidos 📦</strong>
        </h1>
      </div>
      <div className="flex-grow md:overflow-y-auto px-3 py-4 md:px-2 h-full">
        {children}
      </div>
    </div>
  );
}
