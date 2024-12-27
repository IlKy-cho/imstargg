import {BattleMap} from "@/model/BattleMap";
import {BattleEventMode} from "@/model/enums/BattleEventMode";

export interface BattleEvent {
  id: number;
  mode: BattleEventMode;
  map: BattleMap;
}
