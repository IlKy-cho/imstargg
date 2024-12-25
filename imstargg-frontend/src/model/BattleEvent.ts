import {BattleMap} from "@/model/BattleMap";
import {BattleEventModeType} from "@/model/enums/BattleEventMode";

export interface BattleEvent {
  id: number;
  mode: BattleEventModeType;
  map: BattleMap;
}
