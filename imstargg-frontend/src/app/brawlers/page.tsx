import {getBrawlers} from "@/lib/api/brawler";
import {Metadata} from "next";
import {getBrawlerResultStatistics} from "@/lib/api/statistics";
import {BrawlerStatisticsOption} from "@/components/statistics-option";
import React from "react";
import {BrawlerListStatistics} from "@/components/statistics";
import {searchParamsToStatisticsParams, StatisticsSearchParams} from "@/model/statistics/StatisticsParams";
import {yesterdayDate} from "@/lib/date";
import {PageHeader, pageHeaderContainerDefault} from "@/components/page-header";
import {cn} from "@/lib/utils";

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
    statsParams.dateRange,
    statsParams.getTrophyOfType(),
    statsParams.getSoloRankTierOfType()
  );

  return (
    <div className="space-y-2">
      <PageHeader>
        <div className={cn("flex flex-col gap-2", pageHeaderContainerDefault)}>
          <h1 className="sm:text-2xl text-xl font-bold text-zinc-800">
            브롤러
          </h1>
        </div>
      </PageHeader>
      <div className="w-full">
        <BrawlerStatisticsOption
          battleType={statsParams.type}
          dateRange={statsParams.dateRange}
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
