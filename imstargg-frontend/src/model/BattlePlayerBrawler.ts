import {BrawlerRarity} from "@/model/BrawlerRarity";

export default interface BattlePlayerBrawler {
  id: number | null;
  name: string | null;
  rarity: BrawlerRarity | null;
  power: number;
  trophies: number | null;
  trophyChange: number | null;
}