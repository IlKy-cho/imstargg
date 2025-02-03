import {BattlePlayer} from "@/model/BattlePlayer";
import {BattleType} from "@/model/enums/BattleType";
import {BattleResult} from "@/model/enums/BattleResult";
import {BattleMode} from "@/model/enums/BattleMode";
import {PlayerBattleEvent} from "@/model/PlayerBattleEvent";

export interface PlayerBattle {
  battleTime: Date;
  event: PlayerBattleEvent;
  mode: BattleMode;
  type: BattleType;
  result?: BattleResult;
  duration?: number;
  rank?: number;
  trophyChange?: number;
  starPlayerTag: string | null;
  teams: BattlePlayer[][];
}

export function playerBattleMyTeam(battle: PlayerBattle, tag: string): BattlePlayer[] {
  return battle.teams.find((team) => team.some((player) => player.tag === tag)) ?? [];
}

export function playerBattleMe(battle: PlayerBattle, tag: string): BattlePlayer[] {
  return battle.teams.flat().filter((player) => player.tag === tag);
}
