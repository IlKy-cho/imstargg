"use client";

import Image from "next/image";
import {BattleEventStatisticsParam} from "@/model/BattleEventStatisticsParam";
import {BattleEvent} from "@/model/BattleEvent";
import {TrophyRange, trophyRangeTitle, TrophyRangeValues} from "@/model/enums/TrophyRange";
import {Select, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {BrawlStarsIconSrc} from "@/components/icon";
import {useRouter, useSearchParams} from "next/navigation";
import {SoloRankTierRange, soloRankTierRangeTitle, SoloRankTierRangeValues} from "@/model/enums/SoloRankTierRange";

function TrophySelect({trophy}: { trophy?: TrophyRange }) {
  const router = useRouter();
  const searchParams = useSearchParams();

  const handleTrophyChange = (value: string) => {
    const params = new URLSearchParams(searchParams);
    params.set('trophy', value);
    params.delete('soloRankTier');
    router.push(`?${params.toString()}`);
  };

  return (
    <Select defaultValue={trophy} onValueChange={handleTrophyChange}>
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
    router.push(`?${params.toString()}`);
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

type Props = {
  battleEvent: BattleEvent;
  statsParam: BattleEventStatisticsParam;
}

export default function BattleEventRankStatisticsOption(
  {battleEvent, statsParam}: Readonly<Props>
) {
  
}
