import BattlePlayer from "@/model/BattlePlayer";
import BattleEvent from "@/model/BattleEvent";
import {BattleTypeType} from "@/model/enums/BattleType";
import {BattleResultType} from "@/model/enums/BattleResult";

export default interface PlayerBattle {
  battleTime: Date;
  event: BattleEvent | null;
  type: BattleTypeType;
  result: BattleResultType | null;
  duration: number | null;
  rank: number | null;
  trophyChange: number | null;
  starPlayerTag: string | null;
  teams: BattlePlayer[][];
}