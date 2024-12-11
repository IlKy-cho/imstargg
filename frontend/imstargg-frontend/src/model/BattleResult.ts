const BattleResult = {
  DEFEAT: 'DEFEAT',
  VICTORY: 'VICTORY',
  DRAW: 'DRAW',
} as const;

export type BattleResult = typeof BattleResult[keyof typeof BattleResult];
