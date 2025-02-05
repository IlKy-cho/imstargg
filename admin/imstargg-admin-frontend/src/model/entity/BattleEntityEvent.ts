import {BaseEntity} from "@/model/entity/BaseEntity";

export interface BattleEntityEvent extends BaseEntity {
  brawlStarsId: number | null;
  mode: string | null;
  map: string | null;
}