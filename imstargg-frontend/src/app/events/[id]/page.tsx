import {getBattleEvent} from "@/lib/api/battle-event";
import {notFound} from "next/navigation";
import {BattleEventStatisticsOption} from "@/components/statistics-option";
import {
  getBattleEventBrawlerRankStatistics,
  getBattleEventBrawlerResultStatistics,
  getBattleEventBrawlersRankStatistics,
  getBattleEventBrawlersResultStatistics,
  getBattleEventResultBrawlerEnemyStatistics
} from "@/lib/api/statistics";
import {BattleEventModeValue, isResultBattleEventMode} from "@/model/enums/BattleEventMode";
import {
  BrawlerEnemyResultStatistics,
  BrawlerPairRankStatistics,
  BrawlerPairResultStatistics,
  EventBrawlerRankStatistics,
  EventBrawlerResultStatistics
} from "@/components/statistics";
import {getBrawlers} from "@/lib/api/brawler";
import {Brawler} from "@/model/Brawler";
import {
  searchParamsToStatisticsParams,
  StatisticsParams,
  StatisticsSearchParams
} from "@/model/statistics/StatisticsParams";
import {BattleEvent} from "@/model/BattleEvent";
import {battleEventModeIconSrc, battleEventModeTitle} from "@/lib/battle-mode";
import {Suspense} from "react";
import Loading from "@/app/loading";
import {PageHeader, pageHeaderContainerDefault} from "@/components/page-header";
import BattleEventMapImage from "@/components/battle-event-map-image";
import {cn, cnWithDefault} from "@/lib/utils";
import Image from "next/image";
import {BrawlStarsIconSrc} from "@/lib/icon";

type Props = {
  params: Promise<{
    id: number;
  }>;
  searchParams: Promise<StatisticsSearchParams>;
};

export async function generateMetadata({ params }: Readonly<Props>) {
  const { id } = await params;
  const battleEvent = await getBattleEvent(id);
  if (!battleEvent) {
    notFound();
  }
  const mapName = battleEvent.map.name ?? "(이름 없음)";
  const modeTitle = battleEventModeTitle(battleEvent.mode);
  return {
    title: modeTitle ? `${mapName}(${modeTitle})` : mapName,
    description: `브롤스타즈 이벤트 ${battleEvent.mode} ${battleEvent.map.name}의 정보 및 통계입니다.`,
    openGraph: {
      images: battleEvent.map.imageUrl ?? "",
    },
  }
}

export default async function EventPage({ params, searchParams }: Readonly<Props>) {
  const { id } = await params;
  const battleEvent = await getBattleEvent(id);
  if (!battleEvent) {
    notFound();
  }

  const brawlerList = await getBrawlers();
  const statsParams = searchParamsToStatisticsParams(await searchParams);

  return (
    <div className="space-y-2">
      <PageHeader>
        <PageHeaderContent battleEvent={battleEvent} />
      </PageHeader>
      <div className="flex flex-col gap-2 p-1">
        <BattleEventStatisticsOption
          battleEvent={battleEvent}
          battleType={statsParams.type}
          dateRange={statsParams.dateRange}
          trophy={statsParams.trophy}
          soloRankTier={statsParams.soloRankTier}
        />
        <StatisticsContent battleEvent={battleEvent} statsParams={statsParams} brawlers={brawlerList} />
      </div>
    </div>
  );
};

async function StatisticsContent({ battleEvent, statsParams, brawlers }: Readonly<{
  battleEvent: BattleEvent,
  statsParams: StatisticsParams,
  brawlers: Brawler[]
}>) {
  const date = new Date();

  return (
    <div className="flex flex-col sm:flex-row gap-2">
      <div className={cnWithDefault("flex-1 flex flex-col gap-2")}>
        <Title value="브롤러 티어" />
        <Suspense fallback={<Loading />}>
          <PageBrawlerStatistics battleEvent={battleEvent} statsParams={statsParams} date={date} brawlers={brawlers} />
        </Suspense>
      </div>
      <div className="flex flex-col gap-2 sm:w-96">
        {battleEvent.mode !== BattleEventModeValue.SOLO_SHOWDOWN && (
          <div className={cnWithDefault("flex flex-col gap-2")}>
            <Title value="브롤러 시너지" />
            {statsParams.brawlerId ? (
              <Suspense fallback={<Loading />}>
                <PageBrawlersStatistics battleEvent={battleEvent} statsParams={statsParams} date={date} brawlers={brawlers} />
              </Suspense>
            ) : (
              <PageBrawlerNotSelected />
            )}
          </div>
        )}
        {isResultBattleEventMode(battleEvent.mode) && (
          <div className={cnWithDefault("flex flex-col gap-2")}>
            <Title value="브롤러 카운터" />
            {statsParams.brawlerId ? (
              <Suspense fallback={<Loading />}>
                <PageBrawlerEnemyStatistics battleEvent={battleEvent} statsParams={statsParams} date={date}
                  brawlers={brawlers} />
              </Suspense>
            ) : (
              <PageBrawlerNotSelected />
            )}
          </div>
        )}
      </div>

    </div>
  );
}

async function PageHeaderContent({ battleEvent }: Readonly<{ battleEvent: BattleEvent }>) {
  const modeIconSrc = battleEventModeIconSrc(battleEvent.mode);
  const modeTitle = battleEventModeTitle(battleEvent.mode);
  return (
    <div className={cn("flex flex-col gap-1", pageHeaderContainerDefault)}>
      <div className="flex gap-2">
        <BattleEventMapImage battleEventMap={battleEvent.map} size="lg" />
        <div className="flex-1">
          <div className="flex items-center gap-2">
            {modeIconSrc &&
              <Image
                src={modeIconSrc} alt={`${battleEvent.mode} icon`} width={32} height={32}
                className="w-6 sm:w-8 h-6 sm:h-8"
              />
            }
            <span className="text-xl sm:text-2xl font-bold">{modeTitle}</span>
          </div>
          <div className="text-lg sm:text-xl text-zinc-700">
            {battleEvent.map.name ?? '❓'}
          </div>
        </div>
      </div>
    </div>
  );
}

async function Title({ value }: Readonly<{ value: string }>) {
  return (
    <h2 className="text-xl font-bold text-gray-800 border-b-2 border-zinc-500 pb-1 mb-4">
      {value}
    </h2>
  );
}

async function PageBrawlerStatistics({ battleEvent, statsParams, date, brawlers }: Readonly<{
  battleEvent: BattleEvent,
  statsParams: StatisticsParams,
  date: Date,
  brawlers: Brawler[]
}>) {

  return (
    isResultBattleEventMode(battleEvent.mode) ? (
      <EventBrawlerResultStatistics
        statsList={await getBattleEventBrawlerResultStatistics(
          battleEvent.id, date, statsParams.dateRange, statsParams.getTrophyOfType(), statsParams.getSoloRankTierOfType())
      }
        brawlers={brawlers}
        selectedBrawlerId={statsParams.brawlerId} />
    ) : (
      <EventBrawlerRankStatistics
        statsList={await getBattleEventBrawlerRankStatistics(battleEvent.id, date, statsParams.dateRange, statsParams.trophy)}
        brawlers={brawlers} />
    )
  );
}

async function PageBrawlersStatistics({ battleEvent, statsParams, date, brawlers }: Readonly<{
  battleEvent: BattleEvent,
  statsParams: StatisticsParams,
  date: Date,
  brawlers: Brawler[]
}>) {

  return (
    isResultBattleEventMode(battleEvent.mode) ? (
      <BrawlerPairResultStatistics
        statsList={await getBattleEventBrawlersResultStatistics(battleEvent.id, statsParams.brawlerId!, date, statsParams.dateRange, statsParams.getTrophyOfType(), statsParams.getSoloRankTierOfType())}
        brawlers={brawlers} />
    ) : (
      <BrawlerPairRankStatistics
        statsList={await getBattleEventBrawlersRankStatistics(battleEvent.id, statsParams.brawlerId!, date, statsParams.dateRange, statsParams.trophy)}
        brawlers={brawlers} />
    )
  );
}

async function PageBrawlerEnemyStatistics({ battleEvent, statsParams, date, brawlers }: Readonly<{
  battleEvent: BattleEvent,
  statsParams: StatisticsParams,
  date: Date,
  brawlers: Brawler[]
}>) {

  return (
    <BrawlerEnemyResultStatistics
      statsList={await getBattleEventResultBrawlerEnemyStatistics(battleEvent.id, statsParams.brawlerId!, date, statsParams.dateRange, statsParams.getTrophyOfType(), statsParams.getSoloRankTierOfType())}
      brawlers={brawlers} />
  );
}

function PageBrawlerNotSelected() {
  return (
    <div className="flex flex-col items-center justify-center">
      <Image
        src={BrawlStarsIconSrc.STAR_WINGS}
        alt="brawler not selected"
        width={100}
        height={100}
        className="sm:w-24 sm:h-24 w-16 h-16"
      />
      <div className="text-zinc-700">
        브롤러를 선택해주세요.
      </div>
    </div>
  );
}
