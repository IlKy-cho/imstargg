export const BattleResult = {
  DEFEAT: 'DEFEAT',
  VICTORY: 'VICTORY',
  DRAW: 'DRAW',
} as const;

export type BattleResultType = typeof BattleResult[keyof typeof BattleResult];

export const BattleResultValues = Object.keys(BattleResult) as BattleResultType[];
