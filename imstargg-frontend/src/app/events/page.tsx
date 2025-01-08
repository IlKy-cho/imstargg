import {getBattleEvents} from "@/lib/api/battle-event";
import {Metadata} from "next";
import {metadataTitle} from "@/config/site";
import BattleEventList from "@/components/battle-event-list";

export const metadata: Metadata = {
  title: metadataTitle("브롤스타즈 이벤트"),
  description: "브롤스타즈의 이벤트를 확인해보세요.",
};

export default async function EventsPage() {
  const battleEvents = await getBattleEvents();

  return (
    <div className="flex flex-col items-center max-w-7xl mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6 text-zinc-800 border-b-2 border-zinc-200 pb-2">
        이벤트
      </h1>
      <div>
        최근 2주간의 이벤트를 확인할 수 있습니다.
      </div>
      <BattleEventList battleEvents={battleEvents}/>
    </div>
  );
}