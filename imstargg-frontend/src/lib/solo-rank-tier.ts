import BronzeIconSrc from '@/../public/rank/icon_ranked_bronze.png';
import SilverIconSrc from '@/../public/rank/icon_ranked_silver.png';
import GoldIconSrc from '@/../public/rank/icon_ranked_gold.png';
import DiamondIconSrc from '@/../public/rank/icon_ranked_diamond.png';
import MythicIconSrc from '@/../public/rank/icon_ranked_mythic.png';
import LegendaryIconSrc from '@/../public/rank/icon_ranked_legendary.png';
import MastersIconSrc from '@/../public/rank/icon_ranked_masters.png';
import {SoloRankTier, SoloRankTierValue} from "@/model/enums/SoloRankTier";

export const SoloRankTierIconSrc = {
  BRONZE: BronzeIconSrc,
  SILVER: SilverIconSrc,
  GOLD: GoldIconSrc,
  DIAMOND: DiamondIconSrc,
  MYTHIC: MythicIconSrc,
  LEGENDARY: LegendaryIconSrc,
  MASTER: MastersIconSrc,
}

export function soloRankTierIconSrc(tier: SoloRankTier) {
  switch (tier) {
    case SoloRankTierValue.BRONZE_1:
    case SoloRankTierValue.BRONZE_2:
    case SoloRankTierValue.BRONZE_3:
      return SoloRankTierIconSrc.BRONZE;
    case SoloRankTierValue.SILVER_1:
    case SoloRankTierValue.SILVER_2:
    case SoloRankTierValue.SILVER_3:
      return SoloRankTierIconSrc.SILVER;
    case SoloRankTierValue.GOLD_1:
    case SoloRankTierValue.GOLD_2:
    case SoloRankTierValue.GOLD_3:
      return SoloRankTierIconSrc.GOLD;
    case SoloRankTierValue.DIAMOND_1:
    case SoloRankTierValue.DIAMOND_2:
    case SoloRankTierValue.DIAMOND_3:
      return SoloRankTierIconSrc.DIAMOND;
    case SoloRankTierValue.MYTHIC_1:
    case SoloRankTierValue.MYTHIC_2:
    case SoloRankTierValue.MYTHIC_3:
      return SoloRankTierIconSrc.MYTHIC;
    case SoloRankTierValue.LEGENDARY_1:
    case SoloRankTierValue.LEGENDARY_2:
    case SoloRankTierValue.LEGENDARY_3:
      return SoloRankTierIconSrc.LEGENDARY;
    case SoloRankTierValue.MASTER:
      return SoloRankTierIconSrc.MASTER;
  }
}
