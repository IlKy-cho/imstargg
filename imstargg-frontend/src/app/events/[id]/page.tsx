import {getBattleEvent} from "@/lib/api/battle-event";
import {notFound} from "next/navigation";
import {BattleEventStatisticsOption} from "@/components/statistics-option";
import {
  getBattleEventBrawlerRankStatistics,
  getBattleEventBrawlerResultStatistics,
  getBattleEventBrawlersRankStatistics,
  getBattleEventBrawlersResultStatistics
} from "@/lib/api/statistics";
import {BattleEventModeValue, isResultBattleEventMode} from "@/model/enums/BattleEventMode";
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
import {BattleEventProfile} from "@/components/battle-event";
import {PageHeader} from "@/components/page-header";

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
      <PageHeader>
        <BattleEventProfile battleEvent={battleEvent}/>
      </PageHeader>
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
      {battleEvent.mode !== BattleEventModeValue.SOLO_SHOWDOWN && (
        <div className="flex flex-col gap-2">
          <Title value="브롤러 조합"/>
          <Suspense fallback={<Loading/>}>
            <PageBrawlersStatistics battleEvent={battleEvent} statsParams={statsParams} date={date}
                                    brawlers={brawlers}/>
          </Suspense>
        </div>
      )}
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


