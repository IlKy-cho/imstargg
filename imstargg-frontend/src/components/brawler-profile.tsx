import {Brawler} from "@/model/Brawler";
import React from "react";
import BrawlerProfileImage from "@/components/brawler-profile-image";
import {BrawlerClassIcon} from "@/components/brawler-class";
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "@/components/ui/tooltip";
import { cn } from "@/lib/utils";
import { brawlerBackgroundColor } from "./brawler";
import {brawlerRarityTitle} from "@/lib/brawler-rarity";
import {brawlerClassTitle} from "@/lib/brawler-class";



type BrawlerProfileProps = { brawler: Brawler };

export function BrawlerProfile({brawler}: Readonly<BrawlerProfileProps>) {
  return (
    <div className="flex gap-2 p-6 rounded-lg shadow-lg border bg-zinc-100/90 m-2 max-w-lg">
      <BrawlerProfileImage brawler={brawler} size="xl"/>
      <div className="flex flex-col gap-1">
        <div className="flex gap-1 items-center">
          <div className="font-bold sm:text-2xl text-xl">{brawler.name}</div>
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger>
                <BrawlerClassIcon brawlerRole={brawler.role} size="xl"/>
              </TooltipTrigger>
              <TooltipContent>
                {brawlerClassTitle(brawler.role)}
              </TooltipContent>
            </Tooltip>
          </TooltipProvider>
        </div>
        <div>
          <span className={cn("text-zinc-50", "sm:text-base text-sm", "font-bold", "rounded-md", "border", "border-zinc-800", "border-[0.5px]", "px-2", "py-1", "items-center", brawlerBackgroundColor(brawler))}>
            <span className="drop-shadow-[0_1.2px_1.2px_rgba(0,0,0,0.8)]">
              {brawlerRarityTitle(brawler.rarity)}
            </span>
          </span>
        </div>
      </div>
    </div>
  );
}

