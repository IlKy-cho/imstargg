"use client"

import {Brawler, BrawlerCollection} from "@/model/Brawler";
import {PlayerBrawler as PlayerBrawlerModel} from "@/model/PlayerBrawler";
import {ChevronsUpDownIcon} from "lucide-react"
import Image, {StaticImageData} from "next/image";
import {Collapsible, CollapsibleContent, CollapsibleTrigger,} from "@/components/ui/collapsible"
import {useState} from "react";
import {Button} from "./ui/button";
import {cn, cnWithDefault} from "@/lib/utils";
import {PowerLevel} from "./brawler";
import {BrawlStarsIconSrc} from "@/lib/icon";
import {Tooltip, TooltipContent, TooltipProvider, TooltipTrigger} from "./ui/tooltip";
import BrawlerProfileImage from "@/components/brawler-profile-image";
import {BrawlerLink} from "./brawler-link";

interface PlayerBrawlerListProps {
  brawlers: Brawler[];
  playerBrawlers: PlayerBrawlerModel[];
}

export function PlayerBrawlerList({ brawlers, playerBrawlers }: Readonly<PlayerBrawlerListProps>) {

  const [opened, setOpened] = useState(true);
  const brawlerCollection = new BrawlerCollection(brawlers);

  return (
    <div className={cnWithDefault()}>
      <Collapsible open={opened} onOpenChange={setOpened}>
        <CollapsibleTrigger asChild>
          <Button variant="outline" className="w-full">
            플레이어 브롤러 {playerBrawlers.length} / {brawlers.length}
            <ChevronsUpDownIcon className="h-4 w-4" />
          </Button>
        </CollapsibleTrigger>
        <CollapsibleContent className="grid grid-cols-2 sm:grid-cols-4 gap-1 pt-1">
          {playerBrawlers
            .filter((playerBrawler) => brawlerCollection.find(playerBrawler.brawlerId))
            .sort((a, b) => b.trophies - a.trophies)
            .map((playerBrawler) => (
              <PlayerBrawler key={playerBrawler.brawlerId} playerBrawler={playerBrawler} brawler={brawlerCollection.find(playerBrawler.brawlerId)!} />
            ))}
        </CollapsibleContent>
      </Collapsible>
    </div>
  );
}

interface PlayerBrawlerProps {
  playerBrawler: PlayerBrawlerModel;
  brawler: Brawler;
}

function PlayerBrawler({ playerBrawler, brawler }: Readonly<PlayerBrawlerProps>) {
  return (
    <div className="flex flex-col gap-1 rounded border border-zinc-200 p-1 hover:bg-zinc-50 transition-colors">
      <div className="flex flex-row gap-1">
        <BrawlerLink brawler={brawler}>
          <BrawlerProfileImage brawler={brawler} size="xs" />
        </BrawlerLink>
        <div className="flex gap-1 items-center">
          <PowerLevel value={playerBrawler.power} />
          <Image src={BrawlStarsIconSrc.TROPHY} alt="stat icon" className="w-4 sm:w-5" />
          <div className="text-xs sm:text-sm">
            <TooltipProvider>
              <Tooltip>
                <TooltipTrigger>
                  {playerBrawler.trophies}
                </TooltipTrigger>
                <TooltipContent>
                  트로피
                </TooltipContent>
              </Tooltip>
            </TooltipProvider>
            &nbsp;/&nbsp;
            <TooltipProvider>
              <Tooltip>
                <TooltipTrigger>
                  {playerBrawler.highestTrophies}
                </TooltipTrigger>
                <TooltipContent>
                  최고 트로피
                </TooltipContent>
              </Tooltip>
            </TooltipProvider>
          </div>
        </div>
      </div>
      {brawler.gadgets.length > 0 && (
        <div className="flex gap-1">
          {brawler.gadgets.map((gadget) => (
            <PlayerBrawlerItem key={gadget.id} imageUrl={gadget.imageUrl || BrawlStarsIconSrc.GADGET_BASE_EMPTY} title={gadget.name} owned={playerBrawler.gadgetIds.includes(gadget.id)} />
          ))}
        </div>
      )}
      {brawler.starPowers.length > 0 && (
        <div className="flex gap-1">
          {brawler.starPowers.map((starPower) => (
            <PlayerBrawlerItem key={starPower.id} imageUrl={starPower.imageUrl || BrawlStarsIconSrc.STAR_POWER_BASE_EMPTY} title={starPower.name} owned={playerBrawler.starPowerIds.includes(starPower.id)} />
          ))}
        </div>
      )}
      {brawler.gears.length > 0 && (
        <div className="w-fit">
          <div className="grid grid-cols-4 gap-1">
            {brawler.gears.map((gear) => (
              <PlayerBrawlerItem key={gear.id} imageUrl={gear.imageUrl || BrawlStarsIconSrc.GEAR_BASE_EMPTY} title={gear.name} owned={playerBrawler.gearIds.includes(gear.id)} />
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

interface PlayerBrawlerItemProps {
  imageUrl: string | StaticImageData;
  title: string;
  owned: boolean;
}

function PlayerBrawlerItem({ imageUrl, title, owned }: Readonly<PlayerBrawlerItemProps>) {
  return (
    <TooltipProvider>
      <Tooltip>
        <TooltipTrigger>
          <Image
            src={imageUrl}
            alt={title}
            width={24}
            height={24}
            className={cn("sm:w-6 sm:h-6 w-5 h-5", !owned && "filter grayscale opacity-50")}
          />
        </TooltipTrigger>
        <TooltipContent>
          {title}
        </TooltipContent>
      </Tooltip>
    </TooltipProvider>
  )
}