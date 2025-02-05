import {BaseEntity} from "@/model/entity/BaseEntity";
import {GearRarity} from "@/model/enums/GearRarity";

export interface GearEntity extends BaseEntity {
  id: number;
  brawlStarsId: string;
  nameMessageCode: string;
  rarity: GearRarity;
}