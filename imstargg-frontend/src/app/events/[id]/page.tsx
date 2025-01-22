import {getBattleEvent} from "@/lib/api/battle-event";
import {notFound} from "next/navigation";
import BattleEventProfile from "@/components/battle-event-profile";
import {TrophyRange, TrophyRangeValue} from "@/model/enums/TrophyRange";
import {SoloRankTierRange, SoloRankTierRangeValue} from "@/model/enums/SoloRankTierRange";
import {BattleEventStatisticsOption} from "@/components/statistics-option";
import {
  getBattleEventBrawlerRankStatistics,
  getBattleEventBrawlerResultStatistics,
  getBattleEventBrawlersRankStatistics,
  getBattleEventBrawlersResultStatistics
} from "@/lib/api/statistics";
import {isResultBattleEventMode} from "@/model/enums/BattleEventMode";
import {RegularBattleType, RegularBattleTypeValue} from "@/model/enums/BattleType";
import Image from "next/image";
import gusSadPinSrc from "@/../public/icon/brawler/gus/gus_sad_pin.png"
import {
  BattleEventBrawlerRankStatistics,
  BattleEventBrawlerResultStatistics,
  BattleEventBrawlersRankStatistics,
  BattleEventBrawlersResultStatistics
} from "@/components/statistics";
import {getBrawlers} from "@/lib/api/brawler";
import {
  BrawlerResultStatistics as IBrawlerResultStatistics
} from "@/model/statistics/BrawlerResultStatistics";
import {
  BrawlersResultStatistics as IBrawlersResultStatistics
} from "@/model/statistics/BrawlersResultStatistics";
import {
  BrawlerRankStatistics as IBrawlerRankStatistics
} from "@/model/statistics/BrawlerRankStatistics";
import {
  BrawlersRankStatistics as IBrawlersRankStatistics
} from "@/model/statistics/BrawlersRankStatistics";
import {Brawler} from "@/model/Brawler";

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

type SearchParams = {
  duplicateBrawler?: boolean;
  type?: RegularBattleType;
  trophy?: TrophyRange;
  soloRankTier?: SoloRankTierRange;
};

type StatsParams = {
  duplicateBrawler: boolean;
  type: RegularBattleType;
  trophy: TrophyRange;
  soloRankTier: SoloRankTierRange;
};

const hasStatistics = (statistics: Statistics): boolean => {
  return (
    (statistics.brawlerRankStats && statistics.brawlerRankStats.length > 0) ||
    (statistics.brawlerResultStats && statistics.brawlerResultStats.length > 0) ||
    (statistics.brawlersRankStats && statistics.brawlersRankStats.length > 0) ||
    (statistics.brawlersResultStats && statistics.brawlersResultStats.length > 0)
  ) ?? false;
}

const hasBrawlerStats = (statistics: Statistics): boolean => {
  return (
    (statistics.brawlerRankStats && statistics.brawlerRankStats.length > 0) ||
    (statistics.brawlerResultStats && statistics.brawlerResultStats.length > 0)
  ) ?? false;
}

const hasBrawlersStats = (statistics: Statistics): boolean => {
  return (
    (statistics.brawlersRankStats && statistics.brawlersRankStats.length > 0) ||
    (statistics.brawlersResultStats && statistics.brawlersResultStats.length > 0)
  ) ?? false;
}

const hasBrawlerRankStats = (statistics: Statistics): boolean => {
  return (statistics.brawlerRankStats && statistics.brawlerRankStats.length > 0) ?? false;
}

const hasBrawlerResultStats = (statistics: Statistics): boolean => {
  return (statistics.brawlerResultStats && statistics.brawlerResultStats.length > 0) ?? false;
}

const hasBrawlersRankStats = (statistics: Statistics): boolean => {
  return (statistics.brawlersRankStats && statistics.brawlersRankStats.length > 0) ?? false;
}

const hasBrawlersResultStats = (statistics: Statistics): boolean => {
  return (statistics.brawlersResultStats && statistics.brawlersResultStats.length > 0) ?? false;
}

const searchParamsToStatsParams = (searchParams: SearchParams): StatsParams => {
  const duplicateBrawler = searchParams.duplicateBrawler ?? false;
  const type = searchParams.type ?? RegularBattleTypeValue.RANKED;
  const trophy = searchParams.trophy ?? TrophyRangeValue.TROPHY_500_PLUS;
  const soloRankTier = searchParams.soloRankTier ?? SoloRankTierRangeValue.DIAMOND_PLUS;
  return {
    duplicateBrawler,
    type,
    trophy,
    soloRankTier,
  };
}


type Statistics = {
  brawlerRankStats: IBrawlerRankStatistics[] | null;
  brawlersRankStats: IBrawlersRankStatistics[] | null;
  brawlerResultStats: IBrawlerResultStatistics[] | null;
  brawlersResultStats: IBrawlersResultStatistics[] | null;
};

async function fetchStatistics(id: number, date: Date, statsParams: StatsParams, resulted: boolean): Promise<Statistics> {
  const trophyRange = statsParams.type === RegularBattleTypeValue.RANKED ? statsParams.trophy : null;
  const soloRankTier = statsParams.type === RegularBattleTypeValue.SOLO_RANKED ? statsParams.soloRankTier : undefined;

  return {
    brawlerRankStats: !resulted ? await getBattleEventBrawlerRankStatistics(id, date, statsParams.trophy) : null,
    brawlersRankStats: !resulted ? await getBattleEventBrawlersRankStatistics(id, date, statsParams.trophy) : null,
    brawlerResultStats: resulted ? await getBattleEventBrawlerResultStatistics(
      id, date, statsParams.duplicateBrawler, trophyRange, soloRankTier) : null,
    brawlersResultStats: resulted ? await getBattleEventBrawlersResultStatistics(
      id, date, statsParams.duplicateBrawler, trophyRange, soloRankTier) : null,
  };
}


function StatisticsContent({ statistics, brawlerList }: { statistics: Statistics, brawlerList: Brawler[] }) {
  const { brawlerRankStats, brawlersRankStats, brawlerResultStats, brawlersResultStats } = statistics;

  return (
    <div className="flex flex-col lg:flex-row gap-4">
      {
        hasBrawlerStats(statistics) && (
          <div className="flex flex-col gap-2">
            <h2 className="text-xl font-bold text-gray-800 border-b-2 border-zinc-500 pb-1 mb-4">
            브롤러 티어
            </h2>
            {hasBrawlerRankStats(statistics) ? (
              <BattleEventBrawlerRankStatistics
                brawlerRankStatsList={brawlerRankStats!}
                brawlerList={brawlerList}
              />
            ) : null}
            {hasBrawlerResultStats(statistics) ? (
              <BattleEventBrawlerResultStatistics
                brawlerResultStatsList={brawlerResultStats!}
                brawlerList={brawlerList}
              />
            ) : null}
          </div>
        )
      }
      {
        hasBrawlersStats(statistics) && (
          <div className="flex flex-col gap-2">
            <h2 className="text-xl font-bold text-gray-800 border-b-2 border-zinc-500 pb-1 mb-4">
            브롤러 조합
            </h2>
            {hasBrawlersRankStats(statistics) ? (
              <BattleEventBrawlersRankStatistics
                brawlersRankStatsList={brawlersRankStats!}
                brawlerList={brawlerList}
              />
            ) : null}
            {hasBrawlersResultStats(statistics) ? (
              <BattleEventBrawlersResultStatistics
                brawlersResultStatsList={brawlersResultStats!}
                brawlerList={brawlerList}
              />
            ) : null}
          </div>
        )
      }
    </div>
  );
}

type Props = {
  params: Promise<{
    id: number;
  }>;
  searchParams: Promise<SearchParams>;
};

export default async function EventPage({ params, searchParams }: Readonly<Props>) {
  const { id } = await params;
  const battleEvent = await getBattleEvent(id);
  if (!battleEvent) {
    notFound();
  }

  const date = new Date();
  const brawlerList = await getBrawlers();
  const statsParams = searchParamsToStatsParams(await searchParams);
  const resulted = isResultBattleEventMode(battleEvent.mode);
  const statistics = await fetchStatistics(id, date, statsParams, resulted);  

  return (
    <div className="space-y-2">
      <div className="flex flex-col lg:flex-row gap-4 bg-cover bg-center bg-no-repeat bg-brawl-stars-lobby p-4">
        <div className="flex-1">
          <BattleEventProfile battleEvent={battleEvent} />
        </div>
      </div>
      <div>
        <BattleEventStatisticsOption
          battleEvent={battleEvent}
          battleType={statsParams.type}
          duplicateBrawler={statsParams.duplicateBrawler}
          trophy={statsParams.trophy}
          soloRankTier={statsParams.soloRankTier}
        />
      </div>
      <div className="flex justify-center items-center gap-2 m-2 w-full">
        {
          hasStatistics(statistics) ? (
            <StatisticsContent
              statistics={statistics}
              brawlerList={brawlerList}
            />
          ) : (
            <StatisticsAbsence />
          )
        }
      </div>
    </div>
  );
};
