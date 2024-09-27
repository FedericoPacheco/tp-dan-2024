import { ArrowRightIcon } from '@heroicons/react/24/outline';
import Link from 'next/link';
import { lusitana } from './ui/fonts/fonts';
import Image from 'next/image';
import CompanyLogo from './ui/company-logo';

export default function Page() {
  return (
    <main className="flex min-h-screen flex-col p-6 bg-ebony-100">
      {/* Me gustaria agrupar todo esto en un solo componente */}
      <div className="flex items-center align-items:center h-20 shrink-0 items-end rounded-lg bg-ebony-400 p-4 md:h-52">
        <CompanyLogo />
      </div>
      <div className="mt-4 flex grow flex-col gap-4 md:flex-row">
        {/* Me gustaria agrupar todo esto en un solo componente */}
        <div className="flex flex-col justify-center gap-6 rounded-lg bg-ebony-400 px-6 py-10 md:w-2/5 md:px-20">
          <p className={`${lusitana.className} text-[20px] text-white-50`}>
            Bienvenido al sistema de gestion de corralones de Desarrollo de aplicaciones en la nube.
          </p>
          <h1 className={`${lusitana.className} text-[22px] text-white-50`}>
            <strong>Solo para usuarios autorizados</strong>
          </h1>
          <Link
            href="/login"
            className="flex items-center gap-5 self-start rounded-lg bg-white-50 px-6 py-3 text-sm font-medium text-white transition-colors hover:bg-white-800 md:text-base"
          >
            <span>Log in</span> <ArrowRightIcon className="w-5 md:w-6" />
          </Link>
          <div>
            <p className={`${lusitana.className} text-[22px] text-white-50`}>
              ¿Necesitas ayuda? <a href="/login" className="text-white-50 hover:underline">Contactanos</a>
            </p>
          </div>
          <div className='flex items-center justify-center'>
            <div className="relative w-0 h-0 border-l-[15px] border-r-[15px] border-b-[26px] border-l-transparent border-r-transparent border-b-white"/>
          </div>
        </div>
        <div className="flex-row items-center justify-center p-6 ">
          <div className='flex items-center justify-center'>
            <Image 
              src="/ARIDOS.png"
              width={2300}
              height={958}
              className='border border-ebony-400 border-8 rounded-lg'
              alt="materiales de construcción / construction materials"
              priority={true}
            />
          </div>
          <div className='flex flex-col items-center justify-center lg:flex-row'>
            <Image 
              src="/CEMENTO.png"
              width={640}
              height={480}
              className='border border-ebony-400 border-8 rounded-lg'
              alt="cemento  / cement"
            />
            <Image 
              src="/big-brick.png"
              width={640}
              height={480}
              className='border border-ebony-400 border-8 rounded-lg'
              alt="bloque de hormigon/ cement brick"
            />
          </div>
        </div>
      </div>
    </main>
  );
}
