export const revalidate = 60;
// export const dynamic = 'force-dynamic';

import {Metadata} from "next";
import {PageHeader, pageHeaderContainerDefault} from "@/components/page-header";
import {cn, cnWithDefault} from "@/lib/utils";
import {getBattleEvents, getRotationBattleEvents, getSoloRankBattleEvents} from "@/lib/api/battle-event";
import {BattleEventList, GroupedBattleEventList} from "@/components/battle-event";
import {BrawlStarsIconSrc} from "@/lib/icon";
import {Separator} from "@/components/ui/separator";
import Image from "next/image";
import {Suspense} from "react";
import {SwordsIcon} from "lucide-react";
import Loading from "../loading";

export const metadata: Metadata = {
  title: `브롤스타즈 이벤트`,
  description: "브롤스타즈의 이벤트 목록입니다. 각 이벤트의 통계 정보를 확인할 수 있습니다.",
};

export default async function EventsPage() {

  return (
    <div className="space-y-2">
      <PageHeader>
        <div className={cn("flex flex-col gap-2", pageHeaderContainerDefault)}>
          <h1 className="sm:text-2xl text-xl font-bold text-zinc-800">
            이벤트
          </h1>
        </div>
      </PageHeader>
      <div className="p-1 flex flex-col gap-2">
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
          <Separator />
          <Suspense fallback={<Loading />}>
            <PageRotationBattleEventList />
          </Suspense>
        </div>
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
          <Separator />
          <Suspense fallback={<Loading />}>
            <PageSoloRankBattleEventList />
          </Suspense>
        </div>
        <div className={cnWithDefault("p-1 flex flex-col gap-2")}>
          <div className="flex items-center gap-1">
            <SwordsIcon className="sm:h-6 h-5 w-fit" />
            <h2 className="text-lg sm:text-xl font-bold">최근 1주일</h2>
          </div>
          <Separator />
          <Suspense fallback={<Loading />}>
            <PageGroupedBattleEventList />
          </Suspense>
        </div>
      </div>
    </div>
  );
}

export async function PageRotationBattleEventList() {

  const rotationBattleEvents = await getRotationBattleEvents();
  return (
    <BattleEventList battleEvents={rotationBattleEvents.map(event => event.event)}/>
  );
}

export async function PageSoloRankBattleEventList() {
  const battleEvents = await getSoloRankBattleEvents();
  return (
    <BattleEventList battleEvents={battleEvents}/>
  );
}

export async function PageGroupedBattleEventList() {
  const battleEvents = await getBattleEvents();
  return (
    <GroupedBattleEventList battleEvents={battleEvents}/>
  );
}

