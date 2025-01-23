import {getBrawlers} from "@/lib/api/brawler";
import {Metadata} from "next";
import {getBrawlerResultStatistics} from "@/lib/api/statistics";
import {BrawlerStatisticsOption} from "@/components/statistics-option";
import {TrophyRange, TrophyRangeValue} from "@/model/enums/TrophyRange";
import {SoloRankTierRange, SoloRankTierRangeValue} from "@/model/enums/SoloRankTierRange";
import {RegularBattleType, RegularBattleTypeValue} from "@/model/enums/BattleType";
import React from "react";
import {BrawlerListStatistics} from "@/components/statistics";

export const metadata: Metadata = {
  title: `브롤러`,
  description: "브롤스타즈의 모든 브롤러 목록과 통계 정보 입니다.",
};

type SearchParams = {
  type?: RegularBattleType;
  trophy?: TrophyRange;
  soloRankTier?: SoloRankTierRange;
};

type StatsParams = {
  type: RegularBattleType;
  trophy: TrophyRange;
  soloRankTier: SoloRankTierRange;
};

const searchParamsToStatsParams = (searchParams: SearchParams): StatsParams => {
  const type = searchParams.type ?? RegularBattleTypeValue.RANKED;
  const trophy = searchParams.trophy ?? TrophyRangeValue.TROPHY_500_PLUS;
  const soloRankTier = searchParams.soloRankTier ?? SoloRankTierRangeValue.DIAMOND_PLUS;
  return {
    type,
    trophy,
    soloRankTier,
  };
}

type PageProps = {
  searchParams: Promise<SearchParams>;
};

export default async function BrawlersPage({ searchParams }: Readonly<PageProps>) {
  const date = new Date();
  const statsParams = searchParamsToStatsParams(await searchParams);

  const brawlers = await getBrawlers();
  const brawlerResultStats = await getBrawlerResultStatistics(
    date, 
    statsParams.type === RegularBattleTypeValue.RANKED ? statsParams.trophy : null,
    statsParams.type === RegularBattleTypeValue.SOLO_RANKED ? statsParams.soloRankTier : null
  );

  return (
    <div className="flex flex-col gap-4 items-center max-w-7xl mx-auto p-1 md:p-4">
      <h1 className="text-3xl font-bold mb-6 text-zinc-800 border-b-2 border-zinc-200">
        브롤러
      </h1>
      <div className="w-full">
        <BrawlerStatisticsOption
          battleType={statsParams.type}
          trophy={statsParams.trophy}
          soloRankTier={statsParams.soloRankTier}
        />
      </div>
      <div className="w-full">
        <BrawlerListStatistics brawlers={brawlers} statsList={brawlerResultStats} />
      </div>
    </div>
  );
}
