import {BrawlerRarityType} from "@/model/enums/BrawlerRarity";

export interface BattlePlayerBrawler {
  id: number | null;
  name: string | null;
  rarity: BrawlerRarityType | null;
  power: number;
  trophies: number | null;
  trophyChange: number | null;
}