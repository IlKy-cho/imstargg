import BattlePlayer from "@/model/BattlePlayer";

export default interface Battle{
  battleTime: Date;
  event: string;
  type: string;
  result: string;
  duration: number | null;
  rank: number | null;
  trophyChange: number | null;
  starPlayerTag: string | null;
  teams: BattlePlayer[][];
}