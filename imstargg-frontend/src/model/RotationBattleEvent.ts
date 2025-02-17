import {BattleEvent} from "@/model/BattleEvent";

export interface RotationBattleEvent {
  event: BattleEvent;
  startTime: Date;
  endTime: Date;
}