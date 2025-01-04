import BaseEntity from "@/model/entity/BaseEntity";
import {BattleEventMode} from "@/model/enums/BattleEventMode";

export default interface BattleEventEntity extends BaseEntity {
  id: number;
  brawlStarsId: number;
  mode: BattleEventMode;
}