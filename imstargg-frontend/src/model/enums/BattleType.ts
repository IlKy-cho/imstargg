export const BattleTypeValue = {
  NOT_FOUND: 'NOT_FOUND',

  RANKED: 'RANKED',
  SOLO_RANKED: 'SOLO_RANKED',
  FRIENDLY: 'FRIENDLY',
  CHALLENGE: 'CHALLENGE',
  TOURNAMENT: 'TOURNAMENT',
  CHAMPIONSHIP_CHALLENGE: 'CHAMPIONSHIP_CHALLENGE',
} as const;

export type BattleType = typeof BattleTypeValue[keyof typeof BattleTypeValue];

export const BattleTypeValues = Object.keys(BattleTypeValue) as BattleType[];

export const RegularBattleTypeValue = {
  RANKED: BattleTypeValue.RANKED,
  SOLO_RANKED: BattleTypeValue.SOLO_RANKED,
} as const;

export type RegularBattleType = typeof RegularBattleTypeValue[keyof typeof RegularBattleTypeValue];

export const RegularBattleTypeValues = Object.keys(RegularBattleTypeValue) as RegularBattleType[];
