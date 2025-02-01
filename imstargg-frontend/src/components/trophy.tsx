import Image from 'next/image';
import {BrawlStarsIconSrc} from "@/lib/icon";


export default function Trophy({value}: Readonly<{ value: number }>) {
  return (
    <span className="inline-flex items-center gap-0.5">
      <Image
        src={BrawlStarsIconSrc.TROPHY}
        alt="trophy icon"
        className="w-4 sm:w-5"
      />
      <span
        className="text-xs sm:text-sm text-amber-500"
        style={{
          textShadow: '-0.5px -0.5px 0 #000, 0.5px -0.5px 0 #000, -0.5px 0.5px 0 #000, 0.5px 0.5px 0 #000'
        }}
      >
        {value}
      </span>
    </span>
  );
}