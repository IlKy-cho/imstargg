"use client";

import Image from "next/image";
import {BattleEvent} from "@/model/BattleEvent";
import {TrophyRange, trophyRangeTitle, TrophyRangeValues} from "@/model/enums/TrophyRange";
import {Select, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {BrawlStarsIconSrc} from "@/components/icon";
import {useRouter, useSearchParams} from "next/navigation";
import {SoloRankTierRange, soloRankTierRangeTitle, SoloRankTierRangeValues} from "@/model/enums/SoloRankTierRange";
import { useState } from "react";
import { Switch } from "./ui/switch";
import { Label } from "@radix-ui/react-label";
import { Checkbox } from "@radix-ui/react-checkbox";
import { isResultBattleEventMode } from "@/model/enums/BattleEventMode";

function TrophySelect({trophy}: { trophy?: TrophyRange }) {
  const router = useRouter();
  const searchParams = useSearchParams();

  const handleTrophyChange = (value: TrophyRange) => {
    const params = new URLSearchParams(searchParams);
    params.set('trophy', value);
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
        <SelectValue placeholder="트로피"/>
      </SelectTrigger>
      {
        TrophyRangeValues.map((trophyRange) => (
          <SelectItem key={trophyRange} value={trophyRange}>
            <Image
              src={BrawlStarsIconSrc.TROPHY}
              alt="battle type icon"
            />
            {trophyRangeTitle(trophyRange)}
          </SelectItem>
        ))
      }
    </Select>
  )
}

function SoloRankTierSelect({tier}: { tier?: SoloRankTierRange }) {
  const router = useRouter();
  const searchParams = useSearchParams();

  const handleSoloRankTierChange = (value: string) => {
    const params = new URLSearchParams(searchParams);
    params.set('soloRankTier', value);
    params.delete('trophy');
    router.replace(`?${params.toString()}`);
  };

  return (
    <Select defaultValue={tier} onValueChange={handleSoloRankTierChange}>
      <SelectTrigger className="w-32">
        <SelectValue placeholder="경쟁전 티어"/>
      </SelectTrigger>
      {
        SoloRankTierRangeValues.map((tier) => (
          <SelectItem key={tier} value={tier}>
            {soloRankTierRangeTitle(tier)}
          </SelectItem>
        ))
      }
    </Select>
  )
}

function DuplicateBrawlerCheckbox({duplicateBrawler}: { duplicateBrawler: boolean }) {
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
      />
      <Label htmlFor="duplicate-brawler">
        브롤러 중복 허용
      </Label>
    </div>
  )
}

type RankStatsOptionProps = {
  trophy?: TrophyRange;
}

export function BattleEventRankStatisticsOption(
  {trophy}: Readonly<RankStatsOptionProps>
) {
  return (
    <div className="flex gap-2">
      <TrophySelect trophy={trophy} />
    </div>
  );
}

type ResultStatsOptionProps = {
  duplicateBrawler: boolean;
  trophy?: TrophyRange;
  soloRankTier?: SoloRankTierRange;
}

export function BattleEventResultStatisticsOption(
  {duplicateBrawler, trophy, soloRankTier}: Readonly<ResultStatsOptionProps>
) {
  const [soloRanked, setSoloRanked] = useState<boolean>(!!soloRankTier);

  return (
    <div className="flex gap-2">
      <div className="flex items-center space-x-2">
        <Switch
          id="solo-ranked"
          checked={soloRanked}
          onCheckedChange={setSoloRanked}
        />
        <Label htmlFor="solo-ranked">트로피/경쟁전</Label>
      </div>
      {soloRanked ? (
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
  duplicateBrawler: boolean;
  trophy?: TrophyRange;
  soloRankTier?: SoloRankTierRange;
}

export function BattleEventStatsOption({battleEvent, duplicateBrawler, trophy, soloRankTier}: Readonly<StatsOptionProps>) {
  return (
    isResultBattleEventMode(battleEvent.mode) ? (
      <BattleEventResultStatisticsOption
        duplicateBrawler={duplicateBrawler}
        trophy={trophy}
        soloRankTier={soloRankTier}
      />
    ) : (
      <BattleEventRankStatisticsOption trophy={trophy}/>
    )
  );
}