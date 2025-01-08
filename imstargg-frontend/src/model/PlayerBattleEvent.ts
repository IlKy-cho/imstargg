import {BattleEventMode} from "@/model/enums/BattleEventMode";
import {BattleEventMap} from "@/model/BattleEventMap";

export interface PlayerBattleEvent {
  id: number | null;
  mode: BattleEventMode | null;
  map: BattleEventMap;
}