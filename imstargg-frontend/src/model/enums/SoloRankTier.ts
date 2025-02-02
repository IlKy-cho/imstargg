export const SoloRankTierValue = {
  BRONZE_1: 'BRONZE_1',
  BRONZE_2: 'BRONZE_2',
  BRONZE_3: 'BRONZE_3',
  SILVER_1: 'SILVER_1',
  SILVER_2: 'SILVER_2',
  SILVER_3: 'SILVER_3',
  GOLD_1: 'GOLD_1',
  GOLD_2: 'GOLD_2',
  GOLD_3: 'GOLD_3',
  DIAMOND_1: 'DIAMOND_1',
  DIAMOND_2: 'DIAMOND_2',
  DIAMOND_3: 'DIAMOND_3',
  MYTHIC_1: 'MYTHIC_1',
  MYTHIC_2: 'MYTHIC_2',
  MYTHIC_3: 'MYTHIC_3',
  LEGENDARY_1: 'LEGENDARY_1',
  LEGENDARY_2: 'LEGENDARY_2',
  LEGENDARY_3: 'LEGENDARY_3',
  MASTER: 'MASTER',
} as const;

export type SoloRankTier = typeof SoloRankTierValue[keyof typeof SoloRankTierValue];

const tierToValue: Record<SoloRankTier, number> = {
  [SoloRankTierValue.BRONZE_1]: 1,
  [SoloRankTierValue.BRONZE_2]: 2,
  [SoloRankTierValue.BRONZE_3]: 3,
  [SoloRankTierValue.SILVER_1]: 4,
  [SoloRankTierValue.SILVER_2]: 5,
  [SoloRankTierValue.SILVER_3]: 6,
  [SoloRankTierValue.GOLD_1]: 7,
  [SoloRankTierValue.GOLD_2]: 8,
  [SoloRankTierValue.GOLD_3]: 9,
  [SoloRankTierValue.DIAMOND_1]: 10,
  [SoloRankTierValue.DIAMOND_2]: 11,
  [SoloRankTierValue.DIAMOND_3]: 12,
  [SoloRankTierValue.MYTHIC_1]: 13,
  [SoloRankTierValue.MYTHIC_2]: 14,
  [SoloRankTierValue.MYTHIC_3]: 15,
  [SoloRankTierValue.LEGENDARY_1]: 16,
  [SoloRankTierValue.LEGENDARY_2]: 17,
  [SoloRankTierValue.LEGENDARY_3]: 18,
  [SoloRankTierValue.MASTER]: 19,
}

const valueToTier: Record<number, SoloRankTier> = Object.entries(tierToValue).reduce(
  (acc, [tier, value]) => ({
    ...acc,
    [value]: tier as SoloRankTier,
  }),
  {} as Record<number, SoloRankTier>
);

export function soloRankTierValue(tier: SoloRankTier) {
  return tierToValue[tier];
}

export function valueToSoloRankTier(value: number): SoloRankTier {
  return valueToTier[value]!;
}
