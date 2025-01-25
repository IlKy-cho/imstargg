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
        default: 'w-4 h-4',
        lg: 'w-6 h-6',
        xl: 'w-8 h-8',
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
}

export function BrawlerClassIcon({brawlerRole, size}: Readonly<IconProps>) {
  const iconSrc = brawlerClassIconSrc(brawlerRole);

  return <Image
    src={iconSrc}
    alt={`${brawlerRole} class icon`}
    className={cn(iconVariants({size}))}
  />
}
