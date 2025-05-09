import Image from "next/image";
import {cva, VariantProps} from "class-variance-authority";
import React from "react";
import {cn} from "@/lib/utils";
import {BattleEventMap} from "@/model/BattleEventMap";

const imageVariants = cva(
  "",
  {
    variants: {
      size: {
        default: 'sm:h-32 h-24',
        xs: 'sm:h-16 h-12',
        sm: 'sm:h-24 h-16',
        lg: 'sm:h-48 h-32',
        xl: 'sm:h-64 h-48',
      },
    },
    defaultVariants: {
      size: 'default',
    },
  },
);

interface Props
  extends VariantProps<typeof imageVariants> {
  battleEventMap: BattleEventMap;
  className?: string;
}

export default function BattleEventMapImage({battleEventMap, className, size}: Readonly<Props>) {

  return (
    <div className={cn(imageVariants({className, size}))}>
      {battleEventMap.imageUrl ?
        <Image
          src={battleEventMap.imageUrl}
          alt={`${battleEventMap.name} 이미지`}
          width={200}
          height={200}
          className="h-full w-auto object-contain"
        />
        :
        <div className="flex items-center justify-center h-full bg-zinc-300 text-zinc-500 text-md relative" style={{ aspectRatio: '3/5' }}>
          X
        </div>
      }
    </div>
  );

}