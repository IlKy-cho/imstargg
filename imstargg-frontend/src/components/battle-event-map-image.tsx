import {BattleEvent} from "@/model/BattleEvent";
import Image from "next/image";
import {cva, VariantProps} from "class-variance-authority";
import React from "react";
import {cn} from "@/lib/utils";

const imageVariants = cva(
  "",
  {
    variants: {
      size: {
        default: 'h-32',
      },
    },
    defaultVariants: {
      size: 'default',
    },
  },
);

interface Props
  extends React.ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof imageVariants> {
  battleEvent: BattleEvent | null;
}

export default function BattleEventMapImage({battleEvent, className, size}: Readonly<Props>) {

  const mapIconSrc = battleEvent ? battleEvent.map.imageUrl : null;

  return (
    <div className={cn(imageVariants({className, size}))}>
      {mapIconSrc ?
        <Image
          src={mapIconSrc}
          alt={`${battleEvent?.map.name} 이미지`}
          width={200}
          height={200}
          className="h-full w-auto object-contain"
        />
        :
        <div className="flex items-center justify-center h-full bg-gray-200 text-gray-500 text-md relative" style={{ aspectRatio: '3/5' }}>
          X
        </div>
      }
    </div>
  );

}