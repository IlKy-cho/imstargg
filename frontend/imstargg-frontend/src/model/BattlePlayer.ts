import Brawler from "@/model/Brawler";

export default interface BattlePlayer {
  tag: string;
  name: string;
  brawler: Brawler;
  brawlerPower: number;
  brawlerTrophies: number | null;
  brawlerTrophyChange: number | null;
}