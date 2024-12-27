export const BattleTypeValue = {
  NOT_FOUND: 'NOT_FOUND',

  RANKED: 'RANKED',
  SOLO_RANKED: 'SOLO_RANKED',
  FRIENDLY: 'FRIENDLY',
  CHALLENGE: 'CHALLENGE',
} as const;

export type BattleType = typeof BattleTypeValue[keyof typeof BattleTypeValue];

export const BattleTypeValues = Object.keys(BattleTypeValue) as BattleType[];


























