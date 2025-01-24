import {BattleEvent} from "@/model/BattleEvent";

export interface BattleEventResultStatistics {
    event: BattleEvent;
    totalBattleCount: number;
    winRate: number;
    starPlayerRate: number;
}
