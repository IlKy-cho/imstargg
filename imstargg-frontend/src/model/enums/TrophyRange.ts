export const TrophyRangeValue = {
  TROPHY_0_PLUS: 'TROPHY_0_PLUS',
  TROPHY_500_PLUS: 'TROPHY_500_PLUS',
  TROPHY_1000_PLUS: 'TROPHY_1000_PLUS',
} as const;

export type TrophyRange = typeof TrophyRangeValue[keyof typeof TrophyRangeValue];

export const TrophyRangeValues = Object.keys(TrophyRangeValue) as TrophyRange[];

export const trophyRangeTitle = (trophy: TrophyRange): string => {
  switch (trophy) {
    case TrophyRangeValue.TROPHY_0_PLUS:
      return '0+';
    case TrophyRangeValue.TROPHY_500_PLUS:
      return '500+';
    case TrophyRangeValue.TROPHY_1000_PLUS:
      return '1000+';
  }
}
