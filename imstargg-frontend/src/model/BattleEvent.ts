import {BattleEventMap} from "@/model/BattleEventMap";
import {BattleEventMode} from "@/model/enums/BattleEventMode";
import {BattleMode} from "@/model/enums/BattleMode";

export interface BattleEvent {
  id: number;
  mode: BattleEventMode;
  map: BattleEventMap;
  battleMode: BattleMode | null;
}
