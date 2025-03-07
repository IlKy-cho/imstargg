import {BattleEventMap} from "@/model/BattleEventMap";
import {BattleEventEntity} from "@/model/entity/BattleEventEntity";

export interface BattleEvent {
  entity: BattleEventEntity;
  map: BattleEventMap;
  battleMode: string | null;
  latestBattleTime: Date | null;
  soloRanked: boolean;
}