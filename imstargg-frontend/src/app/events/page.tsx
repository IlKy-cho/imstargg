import {getBattleEvents} from "@/lib/api/battle-event";
import {Metadata} from "next";
import {BattleEventList} from "@/components/battle-event";

export const metadata: Metadata = {
  title: `브롤스타즈 이벤트`,
  description: "브롤스타즈의 이벤트 목록입니다. 각 이벤트의 통계 정보를 확인할 수 있습니다.",
};

export default async function EventsPage() {
  const battleEvents = await getBattleEvents();

  return (
    <div className="space-y-2">
      <div className="flex flex-col lg:flex-row gap-4 bg-cover bg-center bg-no-repeat bg-brawl-stars-lobby p-4">
        <div className="flex-1">
          <h1 className="text-3xl font-bold mb-6 text-zinc-800 border-b-2 border-zinc-200 p-2">
            이벤트
          </h1>
        </div>
      </div>
      <div className="w-full p-1 sm:p-4">
        <BattleEventList battleEvents={battleEvents}/>
      </div>
    </div>
  );
}