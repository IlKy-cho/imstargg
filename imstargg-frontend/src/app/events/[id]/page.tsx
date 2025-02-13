import {getBattleEvent} from "@/lib/api/battle-event";
import {notFound} from "next/navigation";
import BattleEventProfile from "@/components/battle-event-profile";
import {BattleEventStatisticsOption} from "@/components/statistics-option";
import {
  getBattleEventBrawlerRankStatistics,
  getBattleEventBrawlerResultStatistics,
  getBattleEventBrawlersRankStatistics,
  getBattleEventBrawlersResultStatistics
} from "@/lib/api/statistics";
import {isResultBattleEventMode} from "@/model/enums/BattleEventMode";
import {
  BrawlerRankStatistics,
  BrawlerResultStatistics,
  BrawlersRankStatistics,
  BrawlersResultStatistics
} from "@/components/statistics";
import {getBrawlers} from "@/lib/api/brawler";
import {Brawler} from "@/model/Brawler";
import {
  searchParamsToStatisticsParams,
  StatisticsParams,
  StatisticsSearchParams
} from "@/model/statistics/StatisticsParams";
import {BattleEvent} from "@/model/BattleEvent";
import {battleEventModeTitle} from "@/lib/battle-mode";
import {Suspense} from "react";
import Loading from "@/app/loading";
import {yesterdayDate} from "@/lib/date";

type Props = {
  params: Promise<{
    id: number;
  }>;
  searchParams: Promise<StatisticsSearchParams>;
};

export async function generateMetadata({params}: Readonly<Props>) {
  const {id} = await params;
  const battleEvent = await getBattleEvent(id);
  if (!battleEvent) {
    notFound();
  }
  return {
    title: `${battleEventModeTitle(battleEvent.mode)} ${battleEvent.map.name}`,
    description: `브롤스타즈 이벤트 ${battleEvent.mode} ${battleEvent.map.name}의 정보 및 통계입니다.`,
    openGraph: {
      images: battleEvent.map.imageUrl || "",
    },
  }
}

export default async function EventPage({params, searchParams}: Readonly<Props>) {
  const {id} = await params;
  const battleEvent = await getBattleEvent(id);
  if (!battleEvent) {
    notFound();
  }


  const brawlerList = await getBrawlers();
  const statsParams = searchParamsToStatisticsParams(await searchParams);

  return (
    <div className="space-y-2">
      <div className="flex flex-col lg:flex-row gap-4 bg-cover bg-center bg-no-repeat bg-brawl-stars-lobby p-4">
        <div className="flex-1">
          <BattleEventProfile battleEvent={battleEvent}/>
        </div>
      </div>
      <div className="flex flex-col gap-2 p-1">
        <BattleEventStatisticsOption
          battleEvent={battleEvent}
          battleType={statsParams.type}
          trophy={statsParams.trophy}
          soloRankTier={statsParams.soloRankTier}
        />
        <StatisticsContent battleEvent={battleEvent} statsParams={statsParams} brawlers={brawlerList}/>
      </div>
    </div>
  );
};

async function StatisticsContent({battleEvent, statsParams, brawlers}: {
  battleEvent: BattleEvent,
  statsParams: StatisticsParams,
  brawlers: Brawler[]
}) {
  const date = yesterdayDate();

  return (
    <div className="flex flex-col gap-4">
      <div className="flex flex-col gap-2">
        <Title value="브롤러 티어"/>
        <Suspense fallback={<Loading/>}>
          <PageBrawlerStatistics battleEvent={battleEvent} statsParams={statsParams} date={date} brawlers={brawlers}/>
        </Suspense>
      </div>
      <div className="flex flex-col gap-2">
        <Title value="브롤러 조합"/>
        <Suspense fallback={<Loading/>}>
          <PageBrawlersStatistics battleEvent={battleEvent} statsParams={statsParams} date={date} brawlers={brawlers}/>
        </Suspense>
      </div>
    </div>
  );
}

async function Title({value}: {value: string}) {
  return (
    <h2 className="text-xl font-bold text-gray-800 border-b-2 border-zinc-500 pb-1 mb-4">
      {value}
    </h2>
  );
}

async function PageBrawlerStatistics({battleEvent, statsParams, date, brawlers}: Readonly<{
  battleEvent: BattleEvent,
  statsParams: StatisticsParams,
  date: Date,
  brawlers: Brawler[]
}>) {

  return (
    isResultBattleEventMode(battleEvent.mode) ? (
      <BrawlerResultStatistics
        statsList={await getBattleEventBrawlerResultStatistics(battleEvent.id, date, statsParams.getTrophyOfType(), statsParams.getSoloRankTierOfType())}
        brawlers={brawlers}/>
    ) : (
      <BrawlerRankStatistics
        statsList={await getBattleEventBrawlerRankStatistics(battleEvent.id, date, statsParams.trophy)}
        brawlers={brawlers}/>
    )
  );
}

async function PageBrawlersStatistics({battleEvent, statsParams, date, brawlers}: Readonly<{
  battleEvent: BattleEvent,
  statsParams: StatisticsParams,
  date: Date,
  brawlers: Brawler[]
}>) {

  return (
    isResultBattleEventMode(battleEvent.mode) ? (
      <BrawlersResultStatistics
        statsList={await getBattleEventBrawlersResultStatistics(battleEvent.id, date, statsParams.getTrophyOfType(), statsParams.getSoloRankTierOfType())}
          brawlers={brawlers}/>
      ) : (
        <BrawlersRankStatistics
          statsList={await getBattleEventBrawlersRankStatistics(battleEvent.id, date, statsParams.trophy)}
          brawlers={brawlers}/>
      )
  );
}


