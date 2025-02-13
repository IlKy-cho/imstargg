import {getBattleEvents} from "@/lib/api/battle-event";
import {Metadata} from "next";
import {BattleEventList} from "@/components/battle-event";
import {PageHeader, pageHeaderContainerDefault} from "@/components/page-header";
import {Help} from "@/components/help";
import {cn} from "@/lib/utils";

export const metadata: Metadata = {
  title: `브롤스타즈 이벤트`,
  description: "브롤스타즈의 이벤트 목록입니다. 각 이벤트의 통계 정보를 확인할 수 있습니다.",
};

export default async function EventsPage() {
  const battleEvents = await getBattleEvents();

  return (
    <div className="space-y-2">
      <PageHeader>
        <div className={cn("flex flex-col gap-2", pageHeaderContainerDefault)}>
          <h1 className="sm:text-2xl text-xl font-bold text-zinc-800">
            이벤트
          </h1>
          <Help description={"최근 1주일의 이벤트입니다."}/>
        </div>
      </PageHeader>
      <div className="w-full p-1 sm:p-4">
        <BattleEventList battleEvents={battleEvents}/>
      </div>
    </div>
  );
}