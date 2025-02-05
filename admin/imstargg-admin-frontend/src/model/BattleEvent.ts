import {BattleEventMap} from "@/model/BattleEventMap";
import {BattleEntityEvent} from "@/model/entity/BattleEntityEvent";

export interface BattleEvent {
  entity: BattleEntityEvent;
  map: BattleEventMap;
  latestBattleTime: Date | null;
}