import {BattleEventMap} from "@/model/BattleEventMap";
import {BattleEventMode} from "@/model/enums/BattleEventMode";

export interface BattleEvent {
  id: number;
  mode: BattleEventMode;
  map: BattleEventMap;
}
