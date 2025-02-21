export interface PlayerRanking {
  tag: string;
  name: string;
  nameColor: string | null;
  clubName: string | null;
  iconId: number;
  trophies: number;
  rank: number;
  rankChange: number | null;
}