import {SoloRankTier, SoloRankTierType} from '@/model/enums/SoloRankTier';

export const BrawlStarsIconSrc = {
  TROPHY: '/icon_trophy.png',
} as const;

export const soloRankTierIconSrc = (tier: SoloRankTierType) => {
  switch (tier) {
    case SoloRankTier.BRONZE_1:
    case SoloRankTier.BRONZE_2:
    case SoloRankTier.BRONZE_3:
      return '/icon_ranked_bronze.png';
    case SoloRankTier.SILVER_1:
    case SoloRankTier.SILVER_2:
    case SoloRankTier.SILVER_3:
      return '/icon_ranked_silver.png';
    case SoloRankTier.GOLD_1:
    case SoloRankTier.GOLD_2:
    case SoloRankTier.GOLD_3:
      return '/icon_ranked_gold.png';
    case SoloRankTier.DIAMOND_1:
    case SoloRankTier.DIAMOND_2:
    case SoloRankTier.DIAMOND_3:
      return '/icon_ranked_diamond.png';
    case SoloRankTier.MYTHIC_1:
    case SoloRankTier.MYTHIC_2:
    case SoloRankTier.MYTHIC_3:
      return '/icon_ranked_mythic.png';
    case SoloRankTier.LEGENDARY_1:
    case SoloRankTier.LEGENDARY_2:
    case SoloRankTier.LEGENDARY_3:
      return '/icon_ranked_legendary.png';
    case SoloRankTier.MASTER:
      return '/icon_ranked_masters.png';
  }
}

