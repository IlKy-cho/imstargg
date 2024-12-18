import BaseEntity from "@/model/entity/BaseEntity";
import {BattleEventModeType} from "@/model/enums/BattleEventMode";

export default interface BattleEventEntity extends BaseEntity {
  id: number;
  brawlStarsId: number;
  mode: BattleEventModeType;
  mapId: number;
}