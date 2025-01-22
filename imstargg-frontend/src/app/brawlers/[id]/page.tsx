import {notFound} from "next/navigation";
import {getBrawlers} from "@/lib/api/brawler";
import {BrawlerProfile} from "@/components/brawler-profile";
import {getBrawlerBrawlersResultStatistics, getBrawlerEnemyResultStatistics} from "@/lib/api/statistics";
import {BrawlerStatisticsOption} from "@/components/statistics-option";
import {
  BrawlerEnemyResultStatistics as BrawlerEnemyResultStatisticsModel
} from "@/model/statistics/BrawlerEnemyResultStatistics";
import {BrawlersResultStatistics as BrawlersResultStatisticsModel} from "@/model/statistics/BrawlersResultStatistics";
import Image from "next/image";
import kitSadPinSrc from "@/../public/icon/brawler/kit/kit_sad_pin.png";
import {BrawlerEnemyResultStatistics, BrawlersResultStatistics} from "@/components/statistics";
import {Brawler, BrawlerCollection} from "@/model/Brawler";
import { searchParamsToStatisticsParams } from "@/model/statistics/StatisticsParams";
import { StatisticsSearchParams } from "@/model/statistics/StatisticsParams";


type Props = {
  params: Promise<{ id: number; }>;
  searchParams: Promise<StatisticsSearchParams>;
};

export default async function BrawlerPage({ params, searchParams }: Readonly<Props>) {
  const { id } = await params;
  const brawlers = await getBrawlers();
  const brawlerCollection = new BrawlerCollection(brawlers);
  const brawler = brawlerCollection.find(id);
  if (!brawler) {
    notFound();
  }

  const date = new Date();
  const statsParams = searchParamsToStatisticsParams(await searchParams);

  const [enemyResultStatistics, brawlersResultStatistics] = await Promise.all([
    getBrawlerEnemyResultStatistics(brawler.id, date, statsParams.getTrophyOfType(), statsParams.getSoloRankTierOfType()),
    getBrawlerBrawlersResultStatistics(brawler.id, date, statsParams.getTrophyOfType(), statsParams.getSoloRankTierOfType())
  ]);

  return (
    <div className="space-y-2">
      <div className="flex flex-col lg:flex-row gap-4 bg-cover bg-center bg-no-repeat bg-brawl-stars-lobby p-4">
        <div className="flex-1">
          <BrawlerProfile brawler={brawler} />
        </div>
      </div>
      <BrawlerStatisticsOption battleType={statsParams.type} trophy={statsParams.trophy} soloRankTier={statsParams.soloRankTier} />
      <StatisticsContent brawlers={brawlers} enemyResultStatistics={enemyResultStatistics} brawlersResultStatistics={brawlersResultStatistics} />
    </div>
  );
}

function StatisticsContent(
  { brawlers, enemyResultStatistics, brawlersResultStatistics }
    : Readonly<{
      brawlers: Brawler[],
      enemyResultStatistics: BrawlerEnemyResultStatisticsModel[],
      brawlersResultStatistics: BrawlersResultStatisticsModel[]
    }>) {

  if (!hasStatistics(enemyResultStatistics, brawlersResultStatistics)) {
    return <StatisticsAbsence />;
  }

  return (
    <div className="flex flex-col gap-4">
      {
        hasStatistics(brawlersResultStatistics) && (
          <div className="flex flex-col gap-2">
            <h2 className="text-xl font-bold text-gray-800 border-b-2 border-zinc-500 pb-1 mb-4">
            브롤러 조합
            </h2>
            <BrawlersResultStatistics statsList={brawlersResultStatistics} brawlers={brawlers} />
          </div>
        )
      }
      {
        hasStatistics(enemyResultStatistics) && (
          <div className="flex flex-col gap-2">
            <h2 className="text-xl font-bold text-gray-800 border-b-2 border-zinc-500 pb-1 mb-4">
            상대 브롤러
            </h2>
            <BrawlerEnemyResultStatistics statsList={enemyResultStatistics} brawlers={brawlers} />
          </div>
        )
      }
    </div>
  );
}

const hasStatistics = (...statisticsArrays: (BrawlerEnemyResultStatisticsModel[] | BrawlersResultStatisticsModel[])[]): boolean => {
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