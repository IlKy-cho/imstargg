import {SoloRankTierValue, SoloRankTier} from "@/model/enums/SoloRankTier";
import {BrawlerRarityValue} from "@/model/enums/BrawlerRarity";
import {Brawler} from "@/model/Brawler";

export const soloRankTierColor = (tier: SoloRankTier) => {
  switch (tier) {
    case SoloRankTierValue.BRONZE_1:
    case SoloRankTierValue.BRONZE_2:
    case SoloRankTierValue.BRONZE_3:
      return '#ECA74F';
    case SoloRankTierValue.SILVER_1:
    case SoloRankTierValue.SILVER_2:
    case SoloRankTierValue.SILVER_3:
      return '#CBD3F5';
    case SoloRankTierValue.GOLD_1:
    case SoloRankTierValue.GOLD_2:
    case SoloRankTierValue.GOLD_3:
      return '#FDF067';
    case SoloRankTierValue.DIAMOND_1:
    case SoloRankTierValue.DIAMOND_2:
    case SoloRankTierValue.DIAMOND_3:
      return '#00F3FC';
    case SoloRankTierValue.MYTHIC_1:
    case SoloRankTierValue.MYTHIC_2:
    case SoloRankTierValue.MYTHIC_3:
      return '#E050F7';
    case SoloRankTierValue.LEGENDARY_1:
    case SoloRankTierValue.LEGENDARY_2:
    case SoloRankTierValue.LEGENDARY_3:
      return '#EB4A59';
    case SoloRankTierValue.MASTER:
      return '#FCEF67';
  }
}

export const brawlerBackgroundColor = (brawler: Brawler) => {
  if (brawler.id === 16000088) {
    return 'bg-[#163df6]';
  }

  switch (brawler.rarity) {
    case BrawlerRarityValue.STARTING_BRAWLER:
      return 'bg-[#A1D5F1]';
    case BrawlerRarityValue.RARE:
      return 'bg-[#6DDB47]';
    case BrawlerRarityValue.SUPER_RARE:
      return 'bg-[#0087fa]';
    case BrawlerRarityValue.EPIC:
      return 'bg-[#AA15E4]';
    case BrawlerRarityValue.MYTHIC:
      return 'bg-[#EA3330]';
    case BrawlerRarityValue.LEGENDARY:
      return 'bg-[#FDF255]';
  }
}