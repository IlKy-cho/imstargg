import {PlayerBattle} from "@/model/PlayerBattle";
import {battleEventModeIconSrc, battleEventModeTitle, battleModeIconSrc, battleModeTitle} from "@/lib/battle-mode";

export function playerBattleIconSrc (battle: PlayerBattle) {
  const eventModeIcon = battle.event.mode ? battleEventModeIconSrc(battle.event.mode) : null;
  return eventModeIcon || battleModeIconSrc(battle.mode);
}

export function playerBattleModeTitle (battle: PlayerBattle) {
  return battle.event.mode && battleEventModeTitle(battle.event.mode) || battleModeTitle(battle.mode);
}