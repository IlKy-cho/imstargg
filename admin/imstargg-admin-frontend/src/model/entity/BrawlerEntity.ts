import {BaseEntity} from "@/model/entity/BaseEntity";
import {BrawlerRarity} from "@/model/enums/BrawlerRarity";
import {BrawlerRole} from "@/model/enums/BrawlerRole";

export interface BrawlerEntity extends BaseEntity {
  id: number;
  brawlStarsId: number,
  nameMessageCode: string
  rarity: BrawlerRarity;
  role: BrawlerRole;
}