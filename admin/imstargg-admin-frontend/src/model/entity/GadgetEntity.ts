import {BaseEntity} from "@/model/entity/BaseEntity";

export interface GadgetEntity extends BaseEntity {
  id: number;
  brawlStarsId: number;
  nameMessageCode: string;
  brawlerId: number;
}