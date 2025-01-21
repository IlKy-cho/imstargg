import {Brawler} from "@/model/Brawler";
import React from "react";
import BrawlerProfileImage from "@/components/brawler-profile-image";
import {BrawlerClassIcon, brawlerClassTitle} from "@/components/brawler-class";
import {brawlerRarityTitle} from "@/components/brawler-rarity";
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "@radix-ui/react-tooltip";
import { cn } from "@/lib/utils";
import { brawlerBackgroundColor } from "./brawler";



type BrawlerProfileProps = { brawler: Brawler };

export function BrawlerProfile({brawler}: Readonly<BrawlerProfileProps>) {
  return (
    <div className="flex gap-2 p-6 rounded-lg shadow-md border bg-zinc-100 bg-opacity-90 m-2 max-w-screen-sm">
      <BrawlerProfileImage brawler={brawler} size="2xl"/>
      <div className="flex flex-col gap-1">
        <div className="flex gap-1 items-center">
          <div className="font-bold text-4xl">{brawler.name}</div>
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger>
                <BrawlerClassIcon brawlerRole={brawler.role} size="xl"/>
              </TooltipTrigger>
              <TooltipContent className="text-sm bg-zinc-800 text-white p-1 rounded-md">
                {brawlerClassTitle(brawler.role)}
              </TooltipContent>
            </Tooltip>
          </TooltipProvider>
        </div>
        <div>
          <span className={cn("text-zinc-50", "text-lg", "font-bold", "rounded-md", "border", "border-zinc-800", "border-[0.5px]", "px-2", "py-1", "items-center", brawlerBackgroundColor(brawler))}>
            <span className="drop-shadow-[0_1.2px_1.2px_rgba(0,0,0,0.8)]">
              {brawlerRarityTitle(brawler.rarity)}
            </span>
          </span>
        </div>
      </div>
    </div>
  );
}

