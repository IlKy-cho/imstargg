import {BrawlerRarityType} from "@/model/enums/BrawlerRarityType";
import {BrawlerRoleType} from "@/model/enums/BrawlerRoleType";
import BaseEntity from "@/model/entity/BaseEntity";

export default interface BrawlerEntity extends BaseEntity {
  id: number;
  brawlStarsId: number,
  nameMessageCode: string
  rarity: BrawlerRarityType;
  role: BrawlerRoleType;
}