import {BattleResult, BattleResultValue} from "@/model/enums/BattleResult";

export function battleResultTitle (result: BattleResult) {
  switch (result) {
    case BattleResultValue.VICTORY:
      return '승리';
    case BattleResultValue.DEFEAT:
      return '패배';
    case BattleResultValue.DRAW:
      return '무승부';
  }
}