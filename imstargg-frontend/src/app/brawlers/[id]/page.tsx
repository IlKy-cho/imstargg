import {notFound} from "next/navigation";
import {getBrawler} from "@/lib/api/brawler";
import {BrawlerProfile} from "@/components/brawler-profile";
import {
  getBrawlerBattleEventResultStatistics,
  getBrawlerBrawlersResultStatistics,
  getBrawlerEnemyResultStatistics
} from "@/lib/api/statistics";
import {BrawlerStatisticsOption, StatisticsParams, StatisticsSearchParams} from "@/components/statistics-option";


type Props = {
  params: Promise<{ id: number; }>;
  searchParams: Promise<StatisticsSearchParams>;
};

export default async function BrawlerPage({params, searchParams}: Readonly<Props>) {
  const {id} = await params;
  const brawler = await getBrawler(id);
  if (!brawler) {
    notFound();
  }

  const date = new Date();
  const statsParams = StatisticsParams.from(await searchParams);

  const [enemyResultStatistics, brawlersResultStatistics, battleEventResultStatistics] = await Promise.all([
    getBrawlerEnemyResultStatistics(brawler.id, date, statsParams.getTrophyOfType(), statsParams.getSoloRankTierOfType()),
    getBrawlerBrawlersResultStatistics(brawler.id, date, statsParams.getTrophyOfType(), statsParams.getSoloRankTierOfType()),
    getBrawlerBattleEventResultStatistics(brawler.id, date, statsParams.getTrophyOfType(), statsParams.getSoloRankTierOfType())
  ]);

  return (
    <div className="space-y-2">
      <div className="flex flex-col lg:flex-row gap-4 bg-cover bg-center bg-no-repeat bg-brawl-stars-lobby p-4">
        <div className="flex-1">
          <BrawlerProfile brawler={brawler}/>
        </div>
        <BrawlerStatisticsOption battleType={statsParams.type} trophy={statsParams.trophy} soloRankTier={statsParams.soloRankTier} />
      </div>
    </div>
  );
}