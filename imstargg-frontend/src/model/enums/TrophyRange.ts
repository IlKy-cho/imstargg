export const TrophyRangeValue = {
  TROPHY_0_PLUS: 'TROPHY_0_PLUS',
  TROPHY_500_PLUS: 'TROPHY_500_PLUS',
  TROPHY_1000_PLUS: 'TROPHY_1000_PLUS',
  TROPHY_1500_PLUS: 'TROPHY_1500_PLUS',
  TROPHY_2000_PLUS: 'TROPHY_2000_PLUS'
} as const;

export type TrophyRange = typeof TrophyRangeValue[keyof typeof TrophyRangeValue];

export const TrophyRangeValues = Object.keys(TrophyRangeValue) as TrophyRange[];
