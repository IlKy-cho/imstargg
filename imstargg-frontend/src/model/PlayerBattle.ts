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

export function playerBattleMyTeam(battle: PlayerBattle): BattlePlayer[] {
  return battle.teams.find((team) => team.some((player) => player.tag === battle.starPlayerTag)) ?? [];
}

export function playerBattleMe(battle: PlayerBattle, tag: string): BattlePlayer[] {
  const myTeam = playerBattleMyTeam(battle);
  return myTeam.filter((player) => player.tag === tag);
}
