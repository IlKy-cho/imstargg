export const SoloRankTierRangeValue = {
  BRONZE_SILVER_GOLD_PLUS: 'BRONZE_SILVER_GOLD_PLUS',
  DIAMOND_PLUS: 'DIAMOND_PLUS',
  MYTHIC_PLUS: 'MYTHIC_PLUS',
  LEGENDARY_PLUS: 'LEGENDARY_PLUS',
  MASTER: 'MASTER',
} as const;

export type SoloRankTierRange = typeof SoloRankTierRangeValue[keyof typeof SoloRankTierRangeValue];

export const SoloRankTierRangeValues = Object.keys(SoloRankTierRangeValue) as SoloRankTierRange[];

export const soloRankTierRangeTitle = (tier: SoloRankTierRange) => {
  switch (tier) {
    case SoloRankTierRangeValue.BRONZE_SILVER_GOLD_PLUS:
      return '브실골+';
    case SoloRankTierRangeValue.DIAMOND_PLUS:
      return '다이아+';
    case SoloRankTierRangeValue.MYTHIC_PLUS:
      return '신화+';
    case SoloRankTierRangeValue.LEGENDARY_PLUS:
      return '전설+';
    case SoloRankTierRangeValue.MASTER:
      return '마스터';
  }
}
