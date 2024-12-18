import {BrawlerRarity} from "@/model/enums/BrawlerRarity";
import {BrawlerRole} from "@/model/enums/BrawlerRole";
import BaseEntity from "@/model/entity/BaseEntity";

export default interface BrawlerEntity extends BaseEntity {
  id: number;
  brawlStarsId: number,
  nameMessageCode: string
  rarity: BrawlerRarity;
  role: BrawlerRole;
}