import BattlePlayerBrawler from "@/model/BattlePlayerBrawler";

export default interface BattlePlayer {
  tag: string;
  name: string;
  brawler: BattlePlayerBrawler;
}