import BattleEventEntity from "@/model/entity/BattleEventEntity";
import BattleEventMap from "@/model/BattleEventMap";

export default interface BattleEvent {
  entity: BattleEventEntity;
  map: BattleEventMap;
  seasoned: boolean;
}