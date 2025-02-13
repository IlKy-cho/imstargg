import {BattleEvent as BattleEventModel} from "@/model/BattleEvent";
import {BattleEventMode, BattleEventModeValues} from "@/model/enums/BattleEventMode";
import Image from 'next/image';
import BattleEventMapImage from "@/components/battle-event-map-image";
import Link from "next/link";
import {battleEventHref} from "@/config/site";
import {
  battleEventModeBackGroundColor,
  battleEventModeIconSrc,
  battleEventModeTitle,
  battleModeIconSrc, battleModeTitle
} from "@/lib/battle-mode";
import {cn} from "@/lib/utils";

type BattleEventListProps = {
  battleEvents: BattleEventModel[];
}

export async function BattleEventList({battleEvents}: Readonly<BattleEventListProps>) {
  const groupedEvents = battleEvents.reduce((groups, event) => {
    const mode = event.mode;
    if (!groups[mode]) {
      groups[mode] = [];
    }
    groups[mode].push(event);
    return groups;
  }, {} as Record<BattleEventMode, BattleEventModel[]>);

  Object.values(groupedEvents).forEach(events => {
    events.sort((a, b) => {
      if (a.map.name === null && b.map.name === null) return 0;
      if (a.map.name === null) return 1;
      if (b.map.name === null) return -1;
      return a.map.name.localeCompare(b.map.name);
    });
  });

  return (
    <>
      {BattleEventModeValues
        .filter(mode => groupedEvents[mode]?.length > 0)
        .map((mode) => (
          <div key={mode} className="mb-4">
            <BattleEventModeHeader mode={mode}/>
            <ul className="mt-2 grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4">
              {groupedEvents[mode].map(event => (
                <li key={event.id}>
                  <BattleEvent battleEvent={event}/>
                </li>
              ))}
            </ul>
          </div>
        ))}
    </>
  );
}

async function BattleEventModeHeader({mode}: Readonly<{ mode: BattleEventMode }>) {
  const backgroundColor = battleEventModeBackGroundColor(mode);
  const iconSrc = battleEventModeIconSrc(mode);
  const title = battleEventModeTitle(mode) || "❓";
  return (
    <div className={cn("flex items-center gap-2 rounded sm:p-2 p-1", backgroundColor)}>
      {iconSrc &&
        <Image
          src={iconSrc}
          alt={`${title} icon`}
          width={24}
          height={24}

        />
      }
      <span className="drop-shadow-md sm:text-xl text-lg font-bold text-white">
        {title}
      </span>
    </div>
  );
}

async function BattleEvent({battleEvent}: Readonly<{ battleEvent: BattleEventModel }>) {
  const headerBackgroundColor = battleEventModeBackGroundColor(battleEvent.mode);
  return (
    <Link href={battleEventHref(battleEvent.id)}>
      <div className="flex flex-col overflow-hidden bg-zinc-100 hover:bg-zinc-200 rounded transition-colors sm:h-60 h-44 sm:w-46 w-38">
        <div className={cn("flex justify-center sm:p-2 p-1", headerBackgroundColor)}>
          <div className="sm:text-sm text-xs font-bold text-white bg-zinc-700 px-1 inline-block rounded">
            {battleEvent.map.name}
          </div>
        </div>
        <div className="flex flex-1 justify-center items-center p-1">
          <BattleEventMapImage battleEventMap={battleEvent.map} size="lg"/>
        </div>
      </div>
    </Link>
  );
}

type BattleEventProfileProps = {
  battleEvent: BattleEventModel;
}

export async function BattleEventProfile({battleEvent}: Readonly<BattleEventProfileProps>) {
  const modeIconSrc = battleEventModeIconSrc(battleEvent.mode) || (battleEvent.battleMode && battleModeIconSrc(battleEvent.battleMode));
  const modeTitle = battleEventModeTitle(battleEvent.mode) || (battleEvent.battleMode && battleModeTitle(battleEvent.battleMode));
  return (
    <div className="flex gap-2 p-4 md:p-6 rounded-lg shadow-lg bg-zinc-100 bg-opacity-90 max-w-lg">
      <BattleEventMapImage battleEventMap={battleEvent.map} size="lg"/>
      <div className="flex-1">
        <div className="flex items-center gap-2">
          {modeIconSrc &&
            <Image
              src={modeIconSrc} alt={`${battleEvent.mode} icon`} width={32} height={32}
              className="w-6 sm:w-8 h-6 sm:h-8"
            />
          }
          <span className="text-xl sm:text-2xl font-bold">{modeTitle}</span>
        </div>
        <div className="text-lg sm:text-xl text-zinc-700">
          {battleEvent.map.name || '❓'}
        </div>
      </div>
    </div>
  );
}
