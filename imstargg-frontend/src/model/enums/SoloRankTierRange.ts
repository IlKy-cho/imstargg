export const SoloRankTierRangeValue = {
  BRONZE_SILVER_GOLD_PLUS: 'BRONZE_SILVER_GOLD_PLUS',
  DIAMOND_PLUS: 'DIAMOND_PLUS',
  MYTHIC_PLUS: 'MYTHIC_PLUS',
  LEGENDARY_MASTER: 'LEGENDARY_MASTER',
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
    case SoloRankTierRangeValue.LEGENDARY_MASTER:
      return '전설+';
    case SoloRankTierRangeValue.MASTER:
      return '마스터';
  }
}
