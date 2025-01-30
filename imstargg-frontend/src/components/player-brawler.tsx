"use client"

import {Brawler, BrawlerCollection} from "@/model/Brawler";
import {PlayerBrawler as PlayerBrawlerModel} from "@/model/PlayerBrawler";
import {ChevronsUpDownIcon} from "lucide-react"
import Image, {StaticImageData} from "next/image";
import {Collapsible, CollapsibleContent, CollapsibleTrigger,} from "@/components/ui/collapsible"
import {useState} from "react";
import {Button} from "./ui/button";
import {cnWithDefault} from "@/lib/utils";
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
        <CollapsibleContent className="flex flex-col gap-1 pt-1">
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
    <div className="rounded border border-zinc-200 p-1 flex justify-between hover:bg-zinc-50 transition-colors">
      <BrawlerLink brawler={brawler}>
        <BrawlerProfileImage brawler={brawler} size="xs" />
      </BrawlerLink>
      <div className="flex gap-1 items-center flex-1 justify-center">
        <PowerLevel value={playerBrawler.power} />
        <BrawlerStat
          icon={BrawlStarsIconSrc.TROPHY}
          current={playerBrawler.trophies}
          total={playerBrawler.highestTrophies}
          tooltipCurrent="트로피"
          tooltipTotal="최고 트로피"
        />
        <BrawlerStat
          icon={BrawlStarsIconSrc.GADGET_BASE_EMPTY}
          current={playerBrawler.gadgetIds.length}
          total={brawler.gadgets.length}
          tooltipCurrent="보유 가젯 수"
          tooltipTotal="브롤러 총 가젯 수"
        />
        <BrawlerStat
          icon={BrawlStarsIconSrc.STAR_POWER_BASE_EMPTY}
          current={playerBrawler.starPowerIds.length}
          total={brawler.starPowers.length}
          tooltipCurrent="보유 스타 파워 수"
          tooltipTotal="브롤러 총 스타 파워 수"
        />
        <BrawlerStat
          icon={BrawlStarsIconSrc.GEAR_BASE_EMPTY}
          current={playerBrawler.gearIds.length}
          total={brawler.gears.length}
          tooltipCurrent="보유 기어 수"
          tooltipTotal="브롤러 총 기어 수"
        />
      </div>
    </div>
  );
}

interface BrawlerStatProps {
  icon: StaticImageData;
  current: number;
  total: number;
  tooltipCurrent: string;
  tooltipTotal: string;
}

function BrawlerStat({ icon, current, total, tooltipCurrent, tooltipTotal }: Readonly<BrawlerStatProps>) {
  return (
    <>
      <Image src={icon} alt="stat icon" className="w-4 sm:w-5" />
      <div className="text-xs sm:text-sm">
        <TooltipProvider>
          <Tooltip>
            <TooltipTrigger>
              {current}
            </TooltipTrigger>
            <TooltipContent>
              {tooltipCurrent}
            </TooltipContent>
          </Tooltip>
        </TooltipProvider>
        &nbsp;/&nbsp;
        <TooltipProvider>
          <Tooltip>
            <TooltipTrigger>
              {total}
            </TooltipTrigger>
            <TooltipContent>
              {tooltipTotal}
            </TooltipContent>
          </Tooltip>
        </TooltipProvider>
      </div>
    </>
  );
}