import {BattlePlayer} from "@/model/BattlePlayer";
import {BattleEvent} from "@/model/BattleEvent";
import {BattleType} from "@/model/enums/BattleType";
import {BattleResult} from "@/model/enums/BattleResult";
import {BattleMode} from "@/model/enums/BattleMode";

export interface PlayerBattle {
  battleTime: Date;
  event: BattleEvent | null;
  mode: BattleMode;
  type: BattleType;
  result?: BattleResult;
  duration?: number;
  rank?: number;
  trophyChange?: number;
  starPlayerTag: string | null;
  teams: BattlePlayer[][];
}