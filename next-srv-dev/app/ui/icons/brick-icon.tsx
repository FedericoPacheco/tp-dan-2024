import Image from 'next/image';

type BrickIconProps = {
    key?: string;
    customClassName?: string;
  };

export default function BrickIcon({key = 'brickLogo',customClassName = 'h-12 w-12'}: BrickIconProps) {
    return (
        <Image
          key={key}
          src={"/icons/brick.svg"}
          className={customClassName}
          width={28}
          height={28}
          alt={'brick logo'}
        />
    );
  }