import {BattleEvent as IBattleEvent} from "@/model/BattleEvent";
import BattleEventMapImage from "@/components/battle-event-map-image";
import Image from "next/image";
import { battleEventModeIconSrc } from "./battle-mode";

type Props = {
  battleEvent: IBattleEvent;
}

export default async function BattleEvent({battleEvent}: Readonly<Props>) {
  return (
    <div className="inline-block bg-zinc-100 rounded p-2">
      <div>
        {battleEvent.map.name}
      </div>
      <BattleEventMapImage battleEventMap={battleEvent.map} size="lg"/>
    </div>
  );
}