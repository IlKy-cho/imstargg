import BattlePlayerBrawler from "@/model/BattlePlayerBrawler";

export interface BattlePlayer {
  tag: string;
  name: string;
  brawler: BattlePlayerBrawler;
}