import BaseEntity from "@/model/entity/BaseEntity";

export default interface BattleEventEntity extends BaseEntity {
  brawlStarsId: number | null;
  mode: string | null;
  map: string | null;
}