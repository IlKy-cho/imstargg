import BattlePlayer from "@/model/BattlePlayer";
import {BattleResult} from "@/model/BattleResult";
import {BattleType} from "@/model/BattleType";
import BattleEvent from "@/model/BattleEvent";

export default interface PlayerBattle {
  battleTime: Date;
  event: BattleEvent | null;
  type: BattleType;
  result: BattleResult | null;
  duration: number | null;
  rank: number | null;
  trophyChange: number | null;
  starPlayerTag: string | null;
  teams: BattlePlayer[][];
}