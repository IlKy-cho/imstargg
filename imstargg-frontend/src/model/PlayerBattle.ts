import {BattlePlayer} from "@/model/BattlePlayer";
import {BattleEvent} from "@/model/BattleEvent";
import {BattleTypeType} from "@/model/enums/BattleType";
import {BattleResultType} from "@/model/enums/BattleResult";
import {BattleModeType} from "@/model/enums/BattleMode";

export interface PlayerBattle {
  battleTime: Date;
  event: BattleEvent | null;
  mode: BattleModeType;
  type: BattleTypeType;
  result?: BattleResultType;
  duration?: number;
  rank?: number;
  trophyChange?: number;
  starPlayerTag: string | null;
  teams: BattlePlayer[][];
}