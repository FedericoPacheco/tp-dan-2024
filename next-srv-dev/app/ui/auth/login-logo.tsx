import { lusitana } from '../fonts/fonts';
import BrickIcon from '../icons/brick-icon';
export default function LoginLogo() {
  return (
    <div
      className={`${lusitana.className} flex justify-center items-center leading-none text-white-50`}
    >
      <BrickIcon customClassName="h-12 w-12 sm:h-16 sm:w-16 md:h-20 md:w-20 lg:h-24 lg:w-24 xl:h-28 xl:w-28" />
      <p className="sm:text-[22px] md:text-[44px] p-3">Login</p>
    </div>
  );
}