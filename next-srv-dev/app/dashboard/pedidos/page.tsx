import { lusitana } from '@/app/ui/fonts/fonts';
import { Metadata } from 'next';


export const metadata: Metadata = {
  title: 'Pedidos',
};

export default async function Page() {  
  return(
    <main>
      <h1 className={`${lusitana.className} mb-4 text-xl md:text-2xl`}>
        {`Se encuentra en pedidos! 🎉`}
      </h1>
    </main>
  );
}