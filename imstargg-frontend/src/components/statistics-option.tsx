"use client";

import Image from "next/image";
import {BattleEvent} from "@/model/BattleEvent";
import {TrophyRange, trophyRangeTitle, TrophyRangeValues} from "@/model/enums/TrophyRange";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {useRouter, useSearchParams} from "next/navigation";
import {SoloRankTierRange, soloRankTierRangeTitle, SoloRankTierRangeValues} from "@/model/enums/SoloRankTierRange";
import {isResultBattleEventMode} from "@/model/enums/BattleEventMode";
import {statisticsBattleTypeTitle} from "@/lib/battle-type";
import {BrawlStarsIconSrc} from "@/lib/icon";
import {StatisticsBattleType, StatisticsBattleTypeValue, StatisticsBattleTypeValues} from "@/model/enums/BattleType";


function BattleTypeSelect({ battleType }: { battleType?: StatisticsBattleType }) {
  const router = useRouter();
  const searchParams = useSearchParams();

  const handleBattleTypeChange = (value: StatisticsBattleType) => {
    const params = new URLSearchParams(searchParams);
    params.set('type', value);
    router.replace(`?${params.toString()}`);
  };

  return (
    <Select defaultValue={battleType} onValueChange={handleBattleTypeChange}>
      <SelectTrigger className="w-28">
        <SelectValue placeholder="전투 타입" />
      </SelectTrigger>
      <SelectContent>
        {
          StatisticsBattleTypeValues.map((type) => (
            <SelectItem key={type} value={type}>
              {statisticsBattleTypeTitle(type)}
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
    params.set('type', StatisticsBattleTypeValue.RANKED);
    router.replace(`?${params.toString()}`);
  };

  return (
    <Select
      defaultValue={trophy}
      onValueChange={handleTrophyChange}
      value={trophy}
    >
      <SelectTrigger className="w-28">
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
    params.set('type', StatisticsBattleTypeValue.SOLO_RANKED);
    router.replace(`?${params.toString()}`);
  };

  return (
    <Select defaultValue={tier} onValueChange={handleSoloRankTierChange}>
      <SelectTrigger className="w-28">
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

function BattleTypeTierSelect(
  { battleType, trophy, soloRankTier }
  : { battleType: StatisticsBattleType, trophy?: TrophyRange, soloRankTier?: SoloRankTierRange }
) {
  return (
    <div className="flex gap-2">
      <BattleTypeSelect battleType={battleType} />
      {battleType === StatisticsBattleTypeValue.SOLO_RANKED ? (
        <SoloRankTierSelect tier={soloRankTier} />
      ) : battleType === StatisticsBattleTypeValue.RANKED ? (
        <TrophySelect trophy={trophy} />
      ) : null}
    </div>
  );
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
  battleType: StatisticsBattleType;
  trophy?: TrophyRange;
  soloRankTier?: SoloRankTierRange;
}

export function BattleEventResultStatisticsOption(
  { battleType, trophy, soloRankTier }: Readonly<ResultStatsOptionProps>
) {

  return (
    <div className="flex gap-2">
      <BattleTypeTierSelect battleType={battleType} trophy={trophy} soloRankTier={soloRankTier} />
    </div>
  )
}

type BattleEventStatsOptionProps = {
  battleEvent: BattleEvent;
  battleType: StatisticsBattleType;
  trophy?: TrophyRange;
  soloRankTier?: SoloRankTierRange;
}

export function BattleEventStatisticsOption({
  battleEvent,
  battleType,
  trophy,
  soloRankTier
}: Readonly<BattleEventStatsOptionProps>) {
  return (
    <div className="border rounded-lg p-1 bg-zinc-100">
      {
        isResultBattleEventMode(battleEvent.mode) ? (
          <BattleEventResultStatisticsOption
            battleType={battleType}
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

type BrawlerStatsOptionProps = {
  battleType: StatisticsBattleType;
  trophy?: TrophyRange;
  soloRankTier?: SoloRankTierRange;
}

export function BrawlerStatisticsOption({
  battleType,
  trophy,
  soloRankTier
}: Readonly<BrawlerStatsOptionProps>) {
  return (
    <div className="border rounded-lg p-1 bg-zinc-100">
      <BattleTypeTierSelect battleType={battleType} trophy={trophy} soloRankTier={soloRankTier} />
    </div>
  )
}