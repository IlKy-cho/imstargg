"use client";

import Image from "next/image";
import { BattleEvent } from "@/model/BattleEvent";
import { TrophyRange, trophyRangeTitle, TrophyRangeValues } from "@/model/enums/TrophyRange";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { BrawlStarsIconSrc } from "@/components/icon";
import { useRouter, useSearchParams } from "next/navigation";
import { SoloRankTierRange, soloRankTierRangeTitle, SoloRankTierRangeValues } from "@/model/enums/SoloRankTierRange";
import { useState } from "react";
import { Label } from "@/components/ui/label";
import { Checkbox } from "@/components/ui/checkbox";
import { isResultBattleEventMode } from "@/model/enums/BattleEventMode";
import {
  RegularBattleType,
  RegularBattleTypeValue,
  RegularBattleTypeValues
} from "@/model/enums/BattleType";
import { battleTypeTitle } from "@/components/battle-type";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip"
import { InfoIcon } from "lucide-react";


function BattleTypeSelect({ battleType }: { battleType?: RegularBattleType }) {
  const router = useRouter();
  const searchParams = useSearchParams();

  const handleBattleTypeChange = (value: RegularBattleType) => {
    const params = new URLSearchParams(searchParams);
    params.set('type', value);
    if (value === RegularBattleTypeValue.SOLO_RANKED) {
      params.delete('trophy');
    } else if (value === RegularBattleTypeValue.RANKED) {
      params.delete('soloRankTier');
    }
    router.replace(`?${params.toString()}`);
  };

  return (
    <Select defaultValue={battleType} onValueChange={handleBattleTypeChange}>
      <SelectTrigger className="w-32">
        <SelectValue placeholder="전투 타입" />
      </SelectTrigger>
      <SelectContent>
        {
          RegularBattleTypeValues.map((type) => (
            <SelectItem key={type} value={type}>
              {battleTypeTitle(type)}
            </SelectItem>
          ))
        }
      </SelectContent>
    </Select>
  )
}

function TrophySelect({ trophy }: { trophy?: TrophyRange }) {
  const router = useRouter();
  const searchParams = useSearchParams();

  const handleTrophyChange = (value: TrophyRange) => {
    const params = new URLSearchParams(searchParams);
    params.set('trophy', value);
    params.set('type', RegularBattleTypeValue.RANKED);
    params.delete('soloRankTier');
    router.replace(`?${params.toString()}`);
  };

  return (
    <Select
      defaultValue={trophy}
      onValueChange={handleTrophyChange}
      value={trophy}
    >
      <SelectTrigger className="w-32">
        <SelectValue placeholder="트로피" />
      </SelectTrigger>
      <SelectContent>
        {
          TrophyRangeValues.map((trophyRange) => (
            <SelectItem key={trophyRange} value={trophyRange}>
              <div className="flex items-center gap-2">
                <Image
                  src={BrawlStarsIconSrc.TROPHY}
                  alt="battle type icon"
                  width={20}
                  height={20}
                />
                <span className="text-sm">{trophyRangeTitle(trophyRange)}</span>
              </div>
            </SelectItem>
          ))
        }
      </SelectContent>
    </Select>
  )
}


function SoloRankTierSelect({ tier }: { tier?: SoloRankTierRange }) {
  const router = useRouter();
  const searchParams = useSearchParams();

  const handleSoloRankTierChange = (value: string) => {
    const params = new URLSearchParams(searchParams);
    params.set('soloRankTier', value);
    params.set('type', RegularBattleTypeValue.SOLO_RANKED);
    params.delete('trophy');
    router.replace(`?${params.toString()}`);
  };

  return (
    <Select defaultValue={tier} onValueChange={handleSoloRankTierChange}>
      <SelectTrigger className="w-32">
        <SelectValue placeholder="경쟁전 티어" />
      </SelectTrigger>
      <SelectContent>
        {
          SoloRankTierRangeValues.map((tier) => (
            <SelectItem key={tier} value={tier}>
              {soloRankTierRangeTitle(tier)}
            </SelectItem>
          ))
        }
      </SelectContent>
    </Select>
  )
}

function DuplicateBrawlerCheckbox({ duplicateBrawler }: { duplicateBrawler: boolean }) {
  const router = useRouter();
  const searchParams = useSearchParams();

  const handleCheckedChange = (checked: boolean) => {
    const params = new URLSearchParams(searchParams);
    if (checked) {
      params.set('duplicateBrawler', 'true');
    } else {
      params.delete('duplicateBrawler');
    }
    router.replace(`?${params.toString()}`);
  };

  return (
    <div className="flex items-center space-x-2">
      <Checkbox
        id="duplicate-brawler"
        checked={duplicateBrawler}
        onCheckedChange={handleCheckedChange}
        className="border-zinc-400"
      />
      <TooltipProvider>
        <Tooltip>
          <TooltipTrigger className="flex items-center gap-1">
            <Label htmlFor="duplicate-brawler" className="text-sm text-zinc-500">
              브롤러 중복
            </Label>
            <InfoIcon className="w-4 h-4 cursor-default" />
          </TooltipTrigger>
          <TooltipContent>
            <p>한 전투에 같은 브롤러가 존재하는 경우가 있습니다.</p>
            <p>중복 브롤러를 포함한 통계를 보고 싶다면 체크해주세요.</p>
          </TooltipContent>
        </Tooltip>
      </TooltipProvider>
    </div>
  )
}

type RankStatsOptionProps = {
  trophy?: TrophyRange;
}

export function BattleEventRankStatisticsOption(
  { trophy }: Readonly<RankStatsOptionProps>
) {
  return (
    <div className="flex gap-2">
      <TrophySelect trophy={trophy} />
    </div>
  );
}

type ResultStatsOptionProps = {
  duplicateBrawler: boolean;
  battleType: RegularBattleType;
  trophy?: TrophyRange;
  soloRankTier?: SoloRankTierRange;
}

export function BattleEventResultStatisticsOption(
  { duplicateBrawler, battleType, trophy, soloRankTier }: Readonly<ResultStatsOptionProps>
) {

  return (
    <div className="flex gap-2">
      <BattleTypeSelect battleType={battleType} />
      {battleType === RegularBattleTypeValue.SOLO_RANKED ? (
        <SoloRankTierSelect tier={soloRankTier} />
      ) : (
        <TrophySelect trophy={trophy} />
      )}
      <DuplicateBrawlerCheckbox duplicateBrawler={duplicateBrawler} />
    </div>
  )
}

type StatsOptionProps = {
  battleEvent: BattleEvent;
  battleType: RegularBattleType;
  duplicateBrawler: boolean;
  trophy?: TrophyRange;
  soloRankTier?: SoloRankTierRange;
}

export function BattleEventStatisticsOption({
  battleEvent,
  battleType,
  duplicateBrawler,
  trophy,
  soloRankTier
}: Readonly<StatsOptionProps>) {
  return (
    <div className="border rounded-lg p-1 bg-zinc-100">
      {
        isResultBattleEventMode(battleEvent.mode) ? (
          <BattleEventResultStatisticsOption
            battleType={battleType}
            duplicateBrawler={duplicateBrawler}
            trophy={trophy}
            soloRankTier={soloRankTier}
          />
        ) : (
          <BattleEventRankStatisticsOption trophy={trophy} />
        )
      }
    </div>
  );
}