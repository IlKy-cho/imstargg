import BattleEventMap from "@/model/BattleEventMap";
import BattleEventEntity from "@/model/entity/BattleEntityEvent";

export default interface BattleEvent {
  entity: BattleEventEntity;
  map: BattleEventMap;
  latestBattleTime: Date | null;
}