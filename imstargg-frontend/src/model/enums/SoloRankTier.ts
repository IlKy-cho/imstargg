export const SoloRankTier = {
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

export type SoloRankTierType = typeof SoloRankTier[keyof typeof SoloRankTier];

export const soloRankTierNumber = (tier: SoloRankTierType) => {
  switch (tier) {
    case SoloRankTier.BRONZE_1:
    case SoloRankTier.SILVER_1:
    case SoloRankTier.GOLD_1:
    case SoloRankTier.DIAMOND_1:
    case SoloRankTier.MYTHIC_1:
    case SoloRankTier.LEGENDARY_1:
      return 'I';
    case SoloRankTier.BRONZE_2:
    case SoloRankTier.SILVER_2:
    case SoloRankTier.GOLD_2:
    case SoloRankTier.DIAMOND_2:
    case SoloRankTier.MYTHIC_2:
    case SoloRankTier.LEGENDARY_2:
      return 'II';
    case SoloRankTier.BRONZE_3:
    case SoloRankTier.SILVER_3:
    case SoloRankTier.GOLD_3:
    case SoloRankTier.DIAMOND_3:
    case SoloRankTier.MYTHIC_3:
    case SoloRankTier.LEGENDARY_3:
      return 'III';
    default:
      return null;
  }
}



