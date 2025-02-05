import {BaseEntity} from "@/model/entity/BaseEntity";

export interface StarPowerEntity extends BaseEntity {
  id: number;
  brawlStarsId: string;
  nameMessageCode: string;
  brawlerId: number;
}