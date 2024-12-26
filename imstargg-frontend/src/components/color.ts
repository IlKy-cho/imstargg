import {SoloRankTier, SoloRankTierType} from "@/model/enums/SoloRankTier";

export const soloRankTierColor = (tier: SoloRankTierType) => {
  switch (tier) {
    case SoloRankTier.BRONZE_1:
    case SoloRankTier.BRONZE_2:
    case SoloRankTier.BRONZE_3:
      return '#ECA74F';
    case SoloRankTier.SILVER_1:
    case SoloRankTier.SILVER_2:
    case SoloRankTier.SILVER_3:
      return '#CBD3F5';
    case SoloRankTier.GOLD_1:
    case SoloRankTier.GOLD_2:
    case SoloRankTier.GOLD_3:
      return '#FDF067';
    case SoloRankTier.DIAMOND_1:
    case SoloRankTier.DIAMOND_2:
    case SoloRankTier.DIAMOND_3:
      return '#00F3FC';
    case SoloRankTier.MYTHIC_1:
    case SoloRankTier.MYTHIC_2:
    case SoloRankTier.MYTHIC_3:
      return '#E050F7';
    case SoloRankTier.LEGENDARY_1:
    case SoloRankTier.LEGENDARY_2:
    case SoloRankTier.LEGENDARY_3:
      return '#EB4A59';
    case SoloRankTier.MASTER:
      return '#FCEF67';
  }
}