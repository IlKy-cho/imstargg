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

export const soloRankTierNumber = (tier: SoloRankTier) => {
  switch (tier) {
    case SoloRankTierValue.BRONZE_1:
    case SoloRankTierValue.SILVER_1:
    case SoloRankTierValue.GOLD_1:
    case SoloRankTierValue.DIAMOND_1:
    case SoloRankTierValue.MYTHIC_1:
    case SoloRankTierValue.LEGENDARY_1:
      return 'I';
    case SoloRankTierValue.BRONZE_2:
    case SoloRankTierValue.SILVER_2:
    case SoloRankTierValue.GOLD_2:
    case SoloRankTierValue.DIAMOND_2:
    case SoloRankTierValue.MYTHIC_2:
    case SoloRankTierValue.LEGENDARY_2:
      return 'II';
    case SoloRankTierValue.BRONZE_3:
    case SoloRankTierValue.SILVER_3:
    case SoloRankTierValue.GOLD_3:
    case SoloRankTierValue.DIAMOND_3:
    case SoloRankTierValue.MYTHIC_3:
    case SoloRankTierValue.LEGENDARY_3:
      return 'III';
    default:
      return null;
  }
}



