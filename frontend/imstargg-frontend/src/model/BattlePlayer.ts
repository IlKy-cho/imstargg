export default interface BattlePlayer {
  tag: string;
  name: string;
  brawler: string;
  brawlerPower: number;
  brawlerTrophies: number | null;
  brawlerTrophyChange: number | null;
}