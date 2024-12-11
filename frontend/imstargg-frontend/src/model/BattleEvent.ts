import {BattleEventMode} from "@/model/BattleEventMode";
import BattleMap from "@/model/BattleMap";

export default interface BattleEvent {
  id: number;
  mode: BattleEventMode;
  map: BattleMap;
}
