import {getBattleEvent} from "@/lib/api/battle-event";
import {notFound} from "next/navigation";
import BattleEventProfile from "@/components/battle-event-profile";

type Props = {
  params: {
    id: number;
  }
};

export default async function EventPage({params}: Readonly<Props>) {
  const {id} = await params;
  const battleEvent = await getBattleEvent(id);
  if (!battleEvent) {
    notFound();
  }

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