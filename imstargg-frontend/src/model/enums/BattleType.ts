export const BattleType = {
  NOT_FOUND: 'NOT_FOUND',

  RANKED: 'RANKED',
  SOLO_RANKED: 'SOLO_RANKED',
  FRIENDLY: 'FRIENDLY',
  CHALLENGE: 'CHALLENGE',
} as const;

export type BattleTypeType = typeof BattleType[keyof typeof BattleType];

export const BattleTypeValues = Object.keys(BattleType) as BattleTypeType[];


























