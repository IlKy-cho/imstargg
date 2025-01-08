import {BattleEvent as IBattleEvent} from "@/model/BattleEvent";
import BattleEventMapImage from "@/components/battle-event-map-image";
import Link from "next/link";

type Props = {
  battleEvent: IBattleEvent;
}

export default async function BattleEvent({battleEvent}: Readonly<Props>) {
  return (
    <Link href={`/events/${battleEvent.id}`}>
      <div className="inline-block bg-zinc-100 hover:bg-zinc-200 rounded p-2 transition-colors">
        <div>
          {battleEvent.map.name}
        </div>
        <BattleEventMapImage battleEventMap={battleEvent.map} size="lg"/>
      </div>
    </Link>
  );
}