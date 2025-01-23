import Image from "next/image";
import {BattleEvent} from "@/model/BattleEvent";
import BattleEventMapImage from "@/components/battle-event-map-image";
import {battleEventModeIconSrc} from "@/components/battle-mode";
import {battleEventModeTitle} from "@/components/title";

type Props = {
  battleEvent: BattleEvent;
}

export default function BattleEventProfile({battleEvent}: Readonly<Props>) {
  const modeIconSrc = battleEventModeIconSrc(battleEvent.mode);
  const modeTitle = battleEventModeTitle(battleEvent.mode);
  return (
    <div className="flex flex-col md:flex-row gap-2 p-4 md:p-6 rounded-lg shadow-md bg-zinc-100 bg-opacity-90 m-2 max-w-screen-sm">
      <BattleEventMapImage battleEventMap={battleEvent.map} size="xl"/>
      <div className="flex-1">
        <div className="flex items-center gap-2 text-2xl md:text-3xl font-bold">
          {modeIconSrc && <Image src={modeIconSrc} alt={`${battleEvent.mode} icon`} width={32} height={32}/>}
          <span>{modeTitle}</span>
        </div>
        <div className="text-xl text-zinc-700">
          {battleEvent.map.name || '❓'}
        </div>
      </div>
    </div>
  );
}