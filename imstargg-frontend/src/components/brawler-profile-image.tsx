import {Brawler} from "@/model/Brawler";
import Image from "next/image";
import {AspectRatio} from "@/components/ui/aspect-ratio";
import {cn} from "@/lib/utils";
import {cva, VariantProps} from "class-variance-authority";
import React from "react";
import {brawlerBackgroundColor} from "@/lib/brawler";


const imageVariants = cva(
  "rounded-[1px] border-[0.5px] border-black",
  {
    variants: {
      size: {
        default: 'sm:w-20 w-16',
        xs: 'sm:w-12 w-10',
        sm: 'sm:w-16 w-12',
        lg: 'sm:w-24 w-18',
        xl: 'sm:w-28 w-20',
        '2xl': 'sm:w-32 w-28',
      },
    },
    defaultVariants: {
      size: 'default',
    },
  },
);

interface ImageProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof imageVariants> {
  brawler?: Brawler | null;
}

export default function BrawlerProfileImage({brawler, className, size}: Readonly<ImageProps>) {

  const backgroundColor = brawler ? brawlerBackgroundColor(brawler) : "bg-gray-200";

  return (
    <div className={cn(imageVariants({ className, size }), backgroundColor)}>
      <AspectRatio ratio={4 / 3}>
        {brawler && brawler.imageUrl ?
          <Image
            src={brawler.imageUrl}
            alt={`${brawler.name} 이미지`}
            sizes="200px"
            fill
            className="h-full w-full object-cover rounded-[1px]"
          />
          :
          <div className="flex items-center justify-center h-full w-full bg-gray-300 text-gray-500 text-sm">
            이미지 없음
          </div>
        }
      </AspectRatio>
    </div>
  );
}
