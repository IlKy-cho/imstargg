import {BaseEntity} from "@/model/entity/BaseEntity";

export interface StarPowerEntity extends BaseEntity {
  id: number;
  brawlStarsId: number;
  nameMessageCode: string;
  brawlerId: number;
}