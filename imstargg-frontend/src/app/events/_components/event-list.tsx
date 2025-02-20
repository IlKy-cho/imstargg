export const revalidate = 60;

import {getBattleEvents, getRotationBattleEvents, getSoloRankBattleEvents} from "@/lib/api/battle-event";
import {BattleEventList, GroupedBattleEventList} from "@/components/battle-event";

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
