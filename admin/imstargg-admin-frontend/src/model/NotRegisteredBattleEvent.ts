export default interface NotRegisteredBattleEvent {
  brawlStarsId: number;
  mode: string | null;
  map: string | null;
  battleTime: Date;
}