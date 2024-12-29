export const BattleResultValue = {
  DEFEAT: 'DEFEAT',
  VICTORY: 'VICTORY',
  DRAW: 'DRAW',
} as const;

export type BattleResult = typeof BattleResultValue[keyof typeof BattleResultValue];

export const BattleResultValues = Object.keys(BattleResultValue) as BattleResult[];
