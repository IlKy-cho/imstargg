import {getBattleEvent} from "@/lib/api/battle-event";
import {notFound} from "next/navigation";
import BattleEventProfile from "@/components/battle-event-profile";
import {TrophyRange, TrophyRangeValue} from "@/model/enums/TrophyRange";
import {SoloRankTierRange, SoloRankTierRangeValue} from "@/model/enums/SoloRankTierRange";
import {BattleEvent} from "@/model/BattleEvent";

const searchParamsToStatisticsParam = (searchParams: SearchParams, battleEvent: BattleEvent) => {
  const date = searchParams.date ?? new Date();
  const duplicateBrawler = searchParams.duplicateBrawler ?? false;
  const trophyRange = searchParams.trophy ?? TrophyRangeValue.TROPHY_500_PLUS;
  const soloRankTierRange = searchParams.soloRankTier ?? SoloRankTierRangeValue.DIAMOND_PLUS;

  return {
    date,
    duplicateBrawler,
    trophyRange,
    soloRankTierRange,
  };
}

type SearchParams = {
  date?: Date;
  duplicateBrawler?: boolean;
  trophy?: TrophyRange;
  soloRankTier?: SoloRankTierRange;
}

type Props = {
  params: {
    id: number;
  }
  searchParams: SearchParams;
};

export default async function EventPage({params, searchParams}: Readonly<Props>) {
  const {id} = await params;
  const battleEvent = await getBattleEvent(id);
  if (!battleEvent) {
    notFound();
  }

  const statsParam = searchParamsToStatisticsParam(searchParams, battleEvent);


  return (
    <div className="space-y-2">
      <div className="flex flex-col lg:flex-row gap-4 bg-cover bg-center bg-no-repeat bg-brawl-stars-lobby p-4">
        <div className="flex-1">
          <BattleEventProfile battleEvent={battleEvent}/>
        </div>
      </div>
      <h1>{battleEvent.mode}</h1>
      <h2>{battleEvent.map.name}</h2>
    </div>
  );
};