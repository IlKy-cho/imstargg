export const revalidate = 60;

import { getBattleEvents } from "@/lib/api/battle-event";
import { getRotationBattleEvents, getSoloRankBattleEvents } from "@/lib/api/battle-event";
import { BrawlStarsIconSrc } from "@/lib/icon";
import { cnWithDefault } from "@/lib/utils";
import Image from "next/image";
import { Separator } from "@/components/ui/separator";
import {BattleEventList, GroupedBattleEventList} from "@/components/battle-event";
import { SwordsIcon } from "lucide-react";

export async function PageRotationBattleEventList() {

  const rotationBattleEvents = await getRotationBattleEvents();
  return (
    <div className={cnWithDefault("p-1 flex flex-col gap-2")}>
      <div className="flex items-center gap-1">
        <Image
          src={BrawlStarsIconSrc.TROPHY}
          width={32}
          height={32}
          alt="trophy-icon"
          className="sm:h-6 h-5 w-fit"
        />
        <h2 className="text-lg sm:text-xl font-bold">진행중</h2>
      </div>
      <Separator/>
      <BattleEventList battleEvents={rotationBattleEvents.map(event => event.event)}/>
    </div>
  )
}

export async function PageSoloRankBattleEventList() {
  const battleEvents = await getSoloRankBattleEvents();
  return (
    <div className={cnWithDefault("p-1 flex flex-col gap-2")}>
      <div className="flex items-center gap-1">
        <Image
          src={BrawlStarsIconSrc.RANKED_FRONT}
          width={32}
          height={32}
          alt="trophy-icon"
          className="sm:h-6 h-5 w-fit"
        />
        <h2 className="text-lg sm:text-xl font-bold">경쟁전</h2>
      </div>
      <Separator/>
      <BattleEventList battleEvents={battleEvents}/>
    </div>
  )
}

export async function PageGroupedBattleEventList() {
  const battleEvents = await getBattleEvents();
  return (
    <div className={cnWithDefault("p-1 flex flex-col gap-2")}>
      <div className="flex items-center gap-1">
        <SwordsIcon className="sm:h-6 h-5 w-fit"/>
        <h2 className="text-lg sm:text-xl font-bold">최근 1주일</h2>
      </div>
      <Separator/>
      <GroupedBattleEventList battleEvents={battleEvents}/>
    </div>
  )
}
