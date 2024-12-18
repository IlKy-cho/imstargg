import {BrawlerRarityType} from "@/model/enums/BrawlerRarity";
import {BrawlerRoleType} from "@/model/enums/BrawlerRole";
import BaseEntity from "@/model/entity/BaseEntity";

export default interface BrawlerEntity extends BaseEntity {
  id: number;
  brawlStarsId: number,
  nameMessageCode: string
  rarity: BrawlerRarityType;
  role: BrawlerRoleType;
}