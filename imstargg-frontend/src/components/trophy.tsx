import Image from 'next/image';
import {BrawlStarsIconSrc} from "@/lib/icon";


export default function Trophy({value}: Readonly<{ value: number }>) {
  return (
    <span className="inline-flex items-center gap-0.5">
      <Image
        src={BrawlStarsIconSrc.TROPHY}
        alt="trophy icon"
        width={20}
        height={20}
        className="h-full w-auto object-contain"
      />
      <span
        className="text-base text-amber-500"
        style={{
          textShadow: '-0.5px -0.5px 0 #000, 0.5px -0.5px 0 #000, -0.5px 0.5px 0 #000, 0.5px 0.5px 0 #000'
        }}
      >
        {value}
      </span>
    </span>
  );
}