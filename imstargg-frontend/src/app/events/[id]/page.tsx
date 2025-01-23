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
import Image from "next/image";
import gusSadPinSrc from "@/../public/icon/brawler/gus/gus_sad_pin.png"
import {
  BrawlerRankStatistics,
  BrawlerResultStatistics,
  BrawlersRankStatistics,
  BrawlersResultStatistics
} from "@/components/statistics";
import {getBrawlers} from "@/lib/api/brawler";
import {BrawlerResultStatistics as BrawlerResultStatisticsModel} from "@/model/statistics/BrawlerResultStatistics";
import {BrawlersResultStatistics as BrawlersResultStatisticsModel} from "@/model/statistics/BrawlersResultStatistics";
import {BrawlerRankStatistics as BrawlerRankStatisticsModel} from "@/model/statistics/BrawlerRankStatistics";
import {BrawlersRankStatistics as BrawlersRankStatisticsModel} from "@/model/statistics/BrawlersRankStatistics";
import {Brawler} from "@/model/Brawler";
import {
  searchParamsToStatisticsParams,
  StatisticsParams,
  StatisticsSearchParams
} from "@/model/statistics/StatisticsParams";
import {battleEventModeTitle} from "@/components/title";
import {BattleEvent} from "@/model/BattleEvent";

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

  const date = new Date();
  const brawlerList = await getBrawlers();
  const statsParams = searchParamsToStatisticsParams(await searchParams);
  const statistics = await fetchStatistics(battleEvent, date, statsParams);

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
          duplicateBrawler={statsParams.duplicateBrawler}
          trophy={statsParams.trophy}
          soloRankTier={statsParams.soloRankTier}
        />
        <StatisticsContent statistics={statistics} brawlers={brawlerList}/>
      </div>
    </div>
  );
};


type Statistics = {
  brawlerRankStats?: BrawlerRankStatisticsModel[];
  brawlersRankStats?: BrawlersRankStatisticsModel[];
  brawlerResultStats?: BrawlerResultStatisticsModel[];
  brawlersResultStats?: BrawlersResultStatisticsModel[];
};

async function fetchStatistics(battleEvent: BattleEvent, date: Date, statsParams: StatisticsParams)
  : Promise<Statistics> {
  if (isResultBattleEventMode(battleEvent.mode)) {
    const [brawlerResultStats, brawlersResultStats] = await Promise.all([
      getBattleEventBrawlerResultStatistics(
        battleEvent.id, date, statsParams.duplicateBrawler, statsParams.getTrophyOfType(), statsParams.getSoloRankTierOfType()),
      getBattleEventBrawlersResultStatistics(
        battleEvent.id, date, statsParams.duplicateBrawler, statsParams.getTrophyOfType(), statsParams.getSoloRankTierOfType())
    ]);
    return {
      brawlerResultStats,
      brawlersResultStats,
    }
  }

  const [brawlerRankStats, brawlersRankStats] = await Promise.all([
    getBattleEventBrawlerRankStatistics(battleEvent.id, date, statsParams.trophy),
    getBattleEventBrawlersRankStatistics(battleEvent.id, date, statsParams.trophy)
  ]);

  return {
    brawlerRankStats,
    brawlersRankStats,
  }
}

function StatisticsContent({statistics, brawlers}: { statistics: Statistics, brawlers: Brawler[] }) {
  const {brawlerRankStats, brawlersRankStats, brawlerResultStats, brawlersResultStats} = statistics;

  if (!hasStatistics(brawlerRankStats, brawlersRankStats, brawlerResultStats, brawlersResultStats)) {
    return <StatisticsAbsence/>;
  }

  return (
    <div className="flex flex-col gap-4">
      <div className="flex flex-col gap-2">
        <h2 className="text-xl font-bold text-gray-800 border-b-2 border-zinc-500 pb-1 mb-4">
          브롤러 티어
        </h2>
        {
          hasStatistics(brawlerRankStats) && (
            <BrawlerRankStatistics statsList={brawlerRankStats!} brawlers={brawlers}/>)
        }
        {
          hasStatistics(brawlerResultStats) && (
            <BrawlerResultStatistics statsList={brawlerResultStats!} brawlers={brawlers}/>
          )
        }
      </div>
      <div className="flex flex-col gap-2">
        <h2 className="text-xl font-bold text-gray-800 border-b-2 border-zinc-500 pb-1 mb-4">
          브롤러 조합
        </h2>
        {
          hasStatistics(brawlersRankStats) && (
            <BrawlersRankStatistics statsList={brawlersRankStats!} brawlers={brawlers}/>
          )
        }
        {
          hasStatistics(brawlersResultStats) && (
            <BrawlersResultStatistics statsList={brawlersResultStats!} brawlers={brawlers}/>
          )
        }
      </div>
    </div>
  );
}


const hasStatistics = (...statisticsList: (BrawlerResultStatisticsModel[] | BrawlersResultStatisticsModel[] | BrawlerRankStatisticsModel[] | BrawlersRankStatisticsModel[] | undefined)[]): boolean => {
  return statisticsList
    .filter(statistics => statistics !== undefined)
    .some(statistics => statistics.length > 0);
}

function StatisticsAbsence() {
  return (
    <div className="flex flex-col items-center justify-center text-center p-4">
      <h1 className="text-2xl font-bold mb-4">데이터가 존재하지 않습니다.</h1>

      <div className="mb-2 text-lg border-b-2 border-zinc-500">
        <p>옵션을 적절히 설정해보세요.</p>
      </div>

      <div className="mb-2">
        <p>그래도 데이터가 존재하지 않는다면</p>
        <p>해당 이벤트의 통계 데이터는</p>
        <p>존재하지 않거나 아직 준비되지 않았습니다.</p>
      </div>

      <div className="mb-2">
        <p>관련 문의사항이 있다면 남겨주세요.</p>
      </div>

      <div className="mb-8">
        <Image
          src={gusSadPinSrc}
          alt="Gus Sad Pin"
        />
      </div>
    </div>
  );
}

