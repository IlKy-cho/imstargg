import {notFound} from "next/navigation";
import {getBrawler, getBrawlers} from "@/lib/api/brawler";
import {BrawlerProfile} from "@/components/brawler-profile";
import {
  getBrawlerBattleEventResultStatistics,
  getBrawlerBrawlersResultStatistics,
  getBrawlerEnemyResultStatistics,
  getBrawlerOwnershipRate
} from "@/lib/api/statistics";
import {BrawlerItemOwnershipOption, BrawlerStatisticsOption} from "@/components/statistics-option";
import {
  BattleEventResultStatistics,
  BrawlerEnemyResultStatistics,
  BrawlersResultStatistics
} from "@/components/statistics";
import {Brawler, BrawlerCollection} from "@/model/Brawler";
import {
  searchParamsToStatisticsParams,
  StatisticsParams,
  StatisticsSearchParams
} from "@/model/statistics/StatisticsParams";
import {Metadata} from "next";
import {getBrawlerRanking} from "@/lib/api/ranking";
import {Country} from "@/model/enums/Country";
import {Ranking} from "@/components/ranking";
import {countryOrDefault} from "@/lib/country";
import {BrawlerGadgetList} from "@/components/gadget";
import {BrawlerStarPowerList} from "@/components/starpower";
import {BrawlerGearList} from "@/components/gear";
import Loading from "@/app/loading";
import {Suspense} from "react";
import {yesterdayDate} from "@/lib/date";
import {PageHeader} from "@/components/page-header";

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

  const statsParams = searchParamsToStatisticsParams(await searchParams);

  const brawlerOwnershipRate = await getBrawlerOwnershipRate(brawler.id, statsParams.trophy);

  return (
    <div className="space-y-2">
      <PageHeader>
        <BrawlerProfile brawler={brawler}/>
      </PageHeader>
      <BrawlerItemOwnershipOption trophy={statsParams.trophy}/>
      <BrawlerGadgetList gadgets={brawler.gadgets} brawlerOwnershipRate={brawlerOwnershipRate}/>
      <BrawlerStarPowerList starPowers={brawler.starPowers} brawlerOwnershipRate={brawlerOwnershipRate}/>
      <BrawlerGearList gears={brawler.gears} brawlerOwnershipRate={brawlerOwnershipRate}/>
      <Ranking rankings={brawlerRankings} country={country}/>
      <div className="flex flex-col gap-2 p-1">
        <BrawlerStatisticsOption battleType={statsParams.type} trophy={statsParams.trophy}
                                 soloRankTier={statsParams.soloRankTier}/>
        <StatisticsContent brawlers={brawlers}
                           brawler={brawler}
                           statsParams={statsParams}
        />
      </div>
    </div>
  );
}

async function Title({value}: Readonly<{ value: string }>) {
  return (
    <h2 className="text-xl sm:text-2xl font-bold text-gray-800 border-b-2 border-zinc-500 pb-1 mb-4">
      {value}
    </h2>
  );
}

async function StatisticsContent(
  {brawler, statsParams, brawlers}
  : Readonly<{
    brawler: Brawler,
    statsParams: StatisticsParams,
    brawlers: Brawler[]
  }>) {

  const date = yesterdayDate();

  return (
    <div className="flex flex-col gap-4">
      <div className="flex flex-col gap-2">
        <Title value="이벤트"/>
        <Suspense fallback={<Loading/>}>
          <PageEventStatistics brawler={brawler} statsParams={statsParams} date={date} brawlers={brawlers}/>
        </Suspense>
      </div>

      <div className="flex flex-col gap-2">
        <Title value="브롤러 조합"/>
        <Suspense fallback={<Loading/>}>
          <PageBrawlersStatistics brawler={brawler} statsParams={statsParams} date={date} brawlers={brawlers}/>
        </Suspense>
      </div>

      <div className="flex flex-col gap-2">
        <Title value="상대 브롤러"/>
        <Suspense fallback={<Loading/>}>
          <PageEnemyStatistics brawler={brawler} statsParams={statsParams} date={date} brawlers={brawlers}/>
        </Suspense>
      </div>
    </div>
  );
}

async function PageEventStatistics({brawler, statsParams, date, brawlers}: Readonly<{
  brawler: Brawler,
  statsParams: StatisticsParams,
  date: Date,
  brawlers: Brawler[]
}>) {

  return (
    <BattleEventResultStatistics statsList={await getBrawlerBattleEventResultStatistics(brawler.id, date, statsParams.getTrophyOfType(), statsParams.getSoloRankTierOfType())}/>
  );
}

async function PageBrawlersStatistics({brawler, statsParams, date, brawlers}: Readonly<{
  brawler: Brawler,
  statsParams: StatisticsParams,
  date: Date,
  brawlers: Brawler[]
}>) {

  return (
    <BrawlersResultStatistics statsList={await getBrawlerBrawlersResultStatistics(brawler.id, date, statsParams.getTrophyOfType(), statsParams.getSoloRankTierOfType())} brawlers={brawlers}/>
  );
}

async function PageEnemyStatistics({brawler, statsParams, date, brawlers}: Readonly<{
  brawler: Brawler,
  statsParams: StatisticsParams,
  date: Date,
  brawlers: Brawler[]
}>) {

  return (
    <BrawlerEnemyResultStatistics statsList={await getBrawlerEnemyResultStatistics(brawler.id, date, statsParams.getTrophyOfType(), statsParams.getSoloRankTierOfType())} brawlers={brawlers}/>
  );
}