import {BattleEvent as IBattleEvent} from "@/model/BattleEvent";
import {BattleEventMode, BattleEventModeValues} from "@/model/enums/BattleEventMode";
import {battleEventModeTitle} from "@/components/title";
import Image from 'next/image';
import {battleEventModeIconSrc} from "@/components/battle-mode";
import BattleEventMapImage from "@/components/battle-event-map-image";
import Link from "next/link";
import {battleEventHref} from "@/config/site";

function BattleEvent({battleEvent}: Readonly<{battleEvent: IBattleEvent}>) {
  return (
    <Link href={battleEventHref(battleEvent.id)}>
      <div className="inline-block bg-zinc-100 hover:bg-zinc-200 rounded p-2 transition-colors">
        <div>
          {battleEvent.map.name}
        </div>
        <BattleEventMapImage battleEventMap={battleEvent.map} size="lg"/>
      </div>
    </Link>
  );
}

function BattleEventModeHeader({ mode }: Readonly<{ mode: BattleEventMode }>) {
  const iconSrc = battleEventModeIconSrc(mode);
  const title = battleEventModeTitle(mode) || "❓";
  return (
    <div className="flex items-center gap-2 bg-zinc-300 rounded p-2 text-2xl font-bold">
      {iconSrc && <Image src={iconSrc} alt={`${title} icon`} width={24} height={24} />}
      {title}
    </div>
  );
}

type Props = {
  battleEvents: IBattleEvent[];
}

export default async function BattleEventList({ battleEvents }: Readonly<Props>) {
  const groupedEvents = battleEvents.reduce((groups, event) => {
    const mode = event.mode;
    if (!groups[mode]) {
      groups[mode] = [];
    }
    groups[mode].push(event);
    return groups;
  }, {} as Record<BattleEventMode, IBattleEvent[]>);

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
            <BattleEventModeHeader mode={mode} />
            <ul className="mt-2 grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4">
              {groupedEvents[mode].map(event => (
                <li key={event.id}>
                  <BattleEvent battleEvent={event} />
                </li>
              ))}
            </ul>
          </div>
        ))}
    </>
  );
}