import LoginForm from '@/app/ui/auth/login-form';
import { Metadata } from 'next';
import LoginLogo from '../ui/auth/login-logo';
import { lusitana } from '@/app/ui/fonts/fonts';

export const metadata: Metadata = {
  title: 'Log-in',
};

export default function LoginPage() {
  return (
    <main className="flex items-center justify-center h-screen bg-ebony-100">
      <div className="relative mx-auto flex w-full max-w-[400px] flex-col space-y-2.5 p-4 md:-mt-32">
        <div className="flex h-20 w-full items-end rounded-lg bg-ebony-400 p-3 md:h-36">
          <LoginLogo />
        </div>
        <LoginForm />
        <div className='flex-col items-center justify-center items-end rounded-lg bg-ebony-400 p-3'>
          <p className={`${lusitana.className} text-[20px] text-white-50 p-2`}>
            Solo <strong>usuarios habilitados</strong> asociados a clientes pueden acceder al sistema
          </p>
          <p className={`${lusitana.className} text-[20px] text-white-50 p-2`}>
            <strong>Solicite cuenta su adminsitrador</strong>
          </p>
        </div>
        <div className='flex items-center justify-center items-end rounded-lg bg-ebony-400 p-3'>
          <p className='text-white-50 p-1'>¿Nuevo en Bricks? =&gt;</p>
          <a className="text-white-50 underline p-2" href="/client-register">
            Registrate
          </a>
        </div>
      </div>
    </main>
  );
}