import {notFound} from "next/navigation";
import {getBrawler, getBrawlers} from "@/lib/api/brawler";
import {BrawlerProfile} from "@/components/brawler-profile";
import {
  getBrawlerBattleEventResultStatistics,
  getBrawlerBrawlersResultStatistics,
  getBrawlerEnemyResultStatistics
} from "@/lib/api/statistics";
import {BrawlerStatisticsOption} from "@/components/statistics-option";
import {
  BrawlerEnemyResultStatistics as BrawlerEnemyResultStatisticsModel
} from "@/model/statistics/BrawlerEnemyResultStatistics";
import {BrawlersResultStatistics as BrawlersResultStatisticsModel} from "@/model/statistics/BrawlersResultStatistics";
import {
  BattleEventResultStatistics as BattleEventResultStatisticsModel
} from "@/model/statistics/BattleEventResultStatistics";
import Image from "next/image";
import kitSadPinSrc from "@/../public/icon/brawler/kit/kit_sad_pin.png";
import {
  BattleEventResultStatistics,
  BrawlerEnemyResultStatistics,
  BrawlersResultStatistics
} from "@/components/statistics";
import {Brawler, BrawlerCollection} from "@/model/Brawler";
import {searchParamsToStatisticsParams, StatisticsSearchParams} from "@/model/statistics/StatisticsParams";
import {Metadata} from "next";
import {getBrawlerRanking} from "@/lib/api/ranking";
import {Country} from "@/model/enums/Country";
import {Ranking} from "@/components/ranking";
import {countryOrDefault} from "@/lib/country";
import {BrawlerGadgetList} from "@/components/gadget";
import { BrawlerStarPowerList } from "@/components/starpower";
import { BrawlerGearList } from "@/components/gear";

interface SearchParams extends StatisticsSearchParams{
  country?: Country;
}

type Props = {
  params: Promise<{ id: number; }>;
  searchParams: Promise<SearchParams>;
};

export async function generateMetadata({params}: Readonly<Props>): Promise<Metadata> {
  const {id} = await params;
  const brawler = await getBrawler(id);
  if (!brawler) {
    notFound();
  }
  return {
    title: `${brawler.name}`,
    description: `브롤스타즈 브롤러 ${brawler.name}의 정보 및 통계입니다.`,
    openGraph: {
      images: brawler.imageUrl || "",
    },
  }
}

export default async function BrawlerPage({params, searchParams}: Readonly<Props>) {
  const {id} = await params;
  const brawlers = await getBrawlers();
  const brawlerCollection = new BrawlerCollection(brawlers);
  const brawler = brawlerCollection.find(id);
  if (!brawler) {
    notFound();
  }

  const country = countryOrDefault((await searchParams).country);
  const brawlerRankings = await getBrawlerRanking(country, brawler.id);

  const date = new Date();
  const statsParams = searchParamsToStatisticsParams(await searchParams);

  const [eventResultStats, enemyResultStats, brawlersResultStats] = await Promise.all([
    getBrawlerBattleEventResultStatistics(brawler.id, date, statsParams.getTrophyOfType(), statsParams.getSoloRankTierOfType()),
    getBrawlerEnemyResultStatistics(brawler.id, date, statsParams.getTrophyOfType(), statsParams.getSoloRankTierOfType()),
    getBrawlerBrawlersResultStatistics(brawler.id, date, statsParams.getTrophyOfType(), statsParams.getSoloRankTierOfType())
  ]);

  return (
    <div className="space-y-2">
      <div className="flex flex-col lg:flex-row gap-4 bg-cover bg-center bg-no-repeat bg-brawl-stars-lobby p-4">
        <div className="flex-1">
          <BrawlerProfile brawler={brawler}/>
        </div>
      </div>
      <BrawlerGadgetList gadgets={brawler.gadgets}/>
      <BrawlerStarPowerList starPowers={brawler.starPowers}/>
      <BrawlerGearList gears={brawler.gears}/>
      <Ranking rankings={brawlerRankings} country={country}/>
      <div className="flex flex-col gap-2 p-1">
        <BrawlerStatisticsOption battleType={statsParams.type} trophy={statsParams.trophy}
                                 soloRankTier={statsParams.soloRankTier}/>
        <StatisticsContent brawlers={brawlers}
                           eventResultStats={eventResultStats}
                           enemyResultStats={enemyResultStats}
                           brawlersResultStats={brawlersResultStats}
        />
      </div>
    </div>
  );
}

function Title({value}: Readonly<{ value: string }>) {
  return (
    <h2 className="text-xl sm:text-2xl font-bold text-gray-800 border-b-2 border-zinc-500 pb-1 mb-4">
      {value}
    </h2>
  );
}

function StatisticsContent(
  {brawlers, eventResultStats, enemyResultStats, brawlersResultStats}
  : Readonly<{
    brawlers: Brawler[],
    eventResultStats: BattleEventResultStatisticsModel[],
    enemyResultStats: BrawlerEnemyResultStatisticsModel[],
    brawlersResultStats: BrawlersResultStatisticsModel[]
  }>) {

  if (!hasStatistics(enemyResultStats, brawlersResultStats)) {
    return <StatisticsAbsence/>;
  }

  return (
    <div className="flex flex-col gap-4">
      <div className="flex flex-col gap-2">
        <Title value="이벤트"/>
        {
          hasStatistics(eventResultStats) && (
            <BattleEventResultStatistics statsList={eventResultStats}/>
          )
        }
      </div>

      <div className="flex flex-col gap-2">
        <Title value="브롤러 조합"/>
        {
          hasStatistics(brawlersResultStats) && (
            <BrawlersResultStatistics statsList={brawlersResultStats} brawlers={brawlers}/>
          )
        }
      </div>

      <div className="flex flex-col gap-2">
        <Title value="상대 브롤러"/>
        {
          hasStatistics(enemyResultStats) && (
            <BrawlerEnemyResultStatistics statsList={enemyResultStats} brawlers={brawlers}/>
          )
        }
      </div>

    </div>
  );
}

const hasStatistics = (...statisticsArrays: (BrawlerEnemyResultStatisticsModel[] | BrawlersResultStatisticsModel[] | BattleEventResultStatisticsModel[])[]): boolean => {
  return statisticsArrays.some(statistics => statistics.length > 0);
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
        <p>해당 브롤러의 통계 데이터는</p>
        <p>아직 준비되지 않았습니다.</p>
      </div>

      <div className="mb-2">
        <p>관련 문의사항이 있다면 남겨주세요.</p>
      </div>

      <div className="mb-8">
        <Image
          src={kitSadPinSrc}
          alt="brawler statistics absence"
        />
      </div>
    </div>
  );
}