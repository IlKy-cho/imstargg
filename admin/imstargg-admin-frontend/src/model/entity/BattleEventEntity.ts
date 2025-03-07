import {BaseEntity} from "@/model/entity/BaseEntity";

export interface BattleEventEntity extends BaseEntity {
  id: number;
  brawlStarsId: number;
  mode: string;
  mapBrawlStarsName: string | null;
  battleMode: string | null;
}