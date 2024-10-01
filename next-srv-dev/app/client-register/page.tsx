import ClientRegisterForm from '@/app/ui/auth/client-register-form';
import { Metadata } from 'next';
import RegisterLogo from "@/app/ui/auth/register-logo";
import { lusitana } from '@/app/ui/fonts/fonts';

export const metadata: Metadata = {
  title: 'Registro de Clientes',
};

export default function ClientRegister() {
  return (
    <main className="flex items-center justify-center h-screen bg-ebony-100">
      <div className="relative mx-auto flex w-full max-w-[600px] flex-col space-y-2.5 p-4 md:-mt-32">
        <div className="flex h-20 w-full items-end rounded-lg bg-ebony-400 p-3 md:h-36">
          <RegisterLogo />
        </div>
        <ClientRegisterForm />
        <div className='flex-col items-center justify-center items-end rounded-lg bg-ebony-400 p-3'>
          <p className={`${lusitana.className} text-[20px] text-white-50 p-2`}>
            La <strong>maxima cantidad de obras en ejecucion</strong> que podrá llevar a cabo una vez registrado sera de 2.
          </p>
          <p className={`${lusitana.className} text-[20px] text-white-50 p-2`}>
            El <strong>maximo descubierto</strong> inical sera de $1000.
          </p>
          <p className={`${lusitana.className} text-[20px] text-white-50 p-2`}>
            <strong>Comunicarse con soporte para su modificacion</strong>
          </p>
        </div>
        <div className='flex items-center justify-center items-end rounded-lg bg-ebony-400 p-3'>
          <p className='text-white-50 p-1'>¿Tiene un usuario habilitado? =&gt;</p>
          <a className="text-white-50 underline p-2" href="/login">
            Ingresar
          </a>
        </div>
      </div>
    </main>
  );
}