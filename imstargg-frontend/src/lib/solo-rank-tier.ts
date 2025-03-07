import BronzeIconSrc from '@/../public/icon/solo-rank/icon_rank_sticker_bronze.webp';
import SilverIconSrc from '@/../public/icon/solo-rank/icon_rank_sticker_silver.webp';
import GoldIconSrc from '@/../public/icon/solo-rank/icon_rank_sticker_gold.webp';
import DiamondIconSrc from '@/../public/icon/solo-rank/icon_rank_sticker_diamond.webp';
import MythicIconSrc from '@/../public/icon/solo-rank/icon_rank_sticker_mythic.webp';
import LegendaryIconSrc from '@/../public/icon/solo-rank/icon_rank_sticker_legendary.webp';
import MastersIconSrc from '@/../public/icon/solo-rank/icon_rank_sticker_masters.webp';
import ProIconSrc from '@/../public/icon/solo-rank/icon_rank_sticker_pro.webp';
import {SoloRankTier, SoloRankTierValue} from "@/model/enums/SoloRankTier";

export const SoloRankTierIconSrc = {
  BRONZE: BronzeIconSrc,
  SILVER: SilverIconSrc,
  GOLD: GoldIconSrc,
  DIAMOND: DiamondIconSrc,
  MYTHIC: MythicIconSrc,
  LEGENDARY: LegendaryIconSrc,
  MASTER: MastersIconSrc,
  PRO: ProIconSrc,
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
    case SoloRankTierValue.MASTER_1:
    case SoloRankTierValue.MASTER_2:
    case SoloRankTierValue.MASTER_3:
      return SoloRankTierIconSrc.MASTER;
    case SoloRankTierValue.PRO:
      return SoloRankTierIconSrc.PRO;
  }
}

export function soloRankTierNumber(tier: SoloRankTier): 'I'| 'II' | 'III' | null {
  switch (tier) {
    case SoloRankTierValue.BRONZE_1:
    case SoloRankTierValue.SILVER_1:
    case SoloRankTierValue.GOLD_1:
    case SoloRankTierValue.DIAMOND_1:
    case SoloRankTierValue.MYTHIC_1:
    case SoloRankTierValue.LEGENDARY_1:
    case SoloRankTierValue.MASTER_1:
      return 'I';
    case SoloRankTierValue.BRONZE_2:
    case SoloRankTierValue.SILVER_2:
    case SoloRankTierValue.GOLD_2:
    case SoloRankTierValue.DIAMOND_2:
    case SoloRankTierValue.MYTHIC_2:
    case SoloRankTierValue.LEGENDARY_2:
    case SoloRankTierValue.MASTER_2:
      return 'II';
    case SoloRankTierValue.BRONZE_3:
    case SoloRankTierValue.SILVER_3:
    case SoloRankTierValue.GOLD_3:
    case SoloRankTierValue.DIAMOND_3:
    case SoloRankTierValue.MYTHIC_3:
    case SoloRankTierValue.LEGENDARY_3:
    case SoloRankTierValue.MASTER_3:
      return 'III';
    case SoloRankTierValue.PRO:
      return null;
  }
}

export function soloRankTierGroupName(tier: SoloRankTier) {
  switch (tier) {
    case SoloRankTierValue.BRONZE_1:
    case SoloRankTierValue.BRONZE_2:
    case SoloRankTierValue.BRONZE_3:
      return '브론즈';
    case SoloRankTierValue.SILVER_1:
    case SoloRankTierValue.SILVER_2:
    case SoloRankTierValue.SILVER_3:
      return '실버';
    case SoloRankTierValue.GOLD_1:
    case SoloRankTierValue.GOLD_2:
    case SoloRankTierValue.GOLD_3:
      return '골드';
    case SoloRankTierValue.DIAMOND_1:
    case SoloRankTierValue.DIAMOND_2:
    case SoloRankTierValue.DIAMOND_3:
      return '다이아';
    case SoloRankTierValue.MYTHIC_1:
    case SoloRankTierValue.MYTHIC_2:
    case SoloRankTierValue.MYTHIC_3:
      return '신화';
    case SoloRankTierValue.LEGENDARY_1:
    case SoloRankTierValue.LEGENDARY_2:
    case SoloRankTierValue.LEGENDARY_3:
      return '전설';
    case SoloRankTierValue.MASTER_1:
    case SoloRankTierValue.MASTER_2:
    case SoloRankTierValue.MASTER_3:
      return '마스터';
    case SoloRankTierValue.PRO:
      return '프로';
  }
}

export function soloRankTierTitle(tier: SoloRankTier) {
  return `${soloRankTierGroupName(tier)}${soloRankTierNumber(tier) || ''}`;
}