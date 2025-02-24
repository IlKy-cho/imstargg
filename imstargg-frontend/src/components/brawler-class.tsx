import {BrawlerRole} from "@/model/enums/BrawlerRole";
import Image from "next/image";
import {cva, VariantProps} from "class-variance-authority";
import React from "react";
import {cn} from "@/lib/utils";
import {brawlerClassIconSrc} from "@/lib/brawler-class";

const iconVariants = cva(
  "",
  {
    variants: {
      size: {
        default: 'sm:w-4 w-3 sm:h-4 h-3',
        lg: 'sm:w-6 w-5 sm:h-6 h-5',
        xl: 'sm:w-8 w-6 sm:h-8 h-6',
      },
    },
    defaultVariants: {
      size: 'default',
    },
  },
);

interface IconProps
  extends VariantProps<typeof iconVariants> {
  brawlerRole: BrawlerRole;
  className?: string;
}

export function BrawlerClassIcon({brawlerRole, size, className}: Readonly<IconProps>) {
  const iconSrc = brawlerClassIconSrc(brawlerRole);

  return <Image
    src={iconSrc}
    alt={`${brawlerRole} class icon`}
    className={cn(iconVariants({size}), className)}
  />
}
