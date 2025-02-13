import {getBrawlers} from "@/lib/api/brawler";
import {Metadata} from "next";
import {getBrawlerResultStatistics} from "@/lib/api/statistics";
import {BrawlerStatisticsOption} from "@/components/statistics-option";
import React from "react";
import {BrawlerListStatistics} from "@/components/statistics";
import {searchParamsToStatisticsParams, StatisticsSearchParams} from "@/model/statistics/StatisticsParams";
import {yesterdayDate} from "@/lib/date";

export const metadata: Metadata = {
  title: `브롤러`,
  description: "브롤스타즈의 모든 브롤러 목록과 통계 정보 입니다.",
};



type PageProps = {
  searchParams: Promise<StatisticsSearchParams>;
};

export default async function BrawlersPage({ searchParams }: Readonly<PageProps>) {
  const yesterday = yesterdayDate();
  
  const statsParams = searchParamsToStatisticsParams(await searchParams);

  const brawlers = await getBrawlers();
  const brawlerResultStats = await getBrawlerResultStatistics(
    yesterday,
    statsParams.getTrophyOfType(),
    statsParams.getSoloRankTierOfType()
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
