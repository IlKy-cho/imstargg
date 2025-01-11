export const SoloRankTierRangeValue = {
  BRONZE_SILVER_GOLD_PLUS: 'BRONZE_SILVER_GOLD_PLUS',
  DIAMOND_PLUS: 'DIAMOND_PLUS',
  MYTHIC_PLUS: 'MYTHIC_PLUS',
  LEGENDARY_MASTER: 'LEGENDARY_MASTER',
  MASTER: 'MASTER',
} as const;

export type SoloRankTierRange = typeof SoloRankTierRangeValue[keyof typeof SoloRankTierRangeValue];

export const SoloRankTierRangeValues = Object.keys(SoloRankTierRangeValue) as SoloRankTierRange[];
