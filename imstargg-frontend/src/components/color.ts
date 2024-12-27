import {SoloRankTier, SoloRankTierType} from "@/model/enums/SoloRankTier";
import {BrawlerRarity} from "@/model/enums/BrawlerRarity";
import {Brawler} from "@/model/Brawler";

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

export const brawlerBackgroundColor = (brawler: Brawler) => {
  if (brawler.id === 16000088) {
    return 'bg-[#163df6]';
  }

  switch (brawler.rarity) {
    case BrawlerRarity.STARTING_BRAWLER:
      return 'bg-[#A1D5F1]';
    case BrawlerRarity.RARE:
      return 'bg-[#6DDB47]';
    case BrawlerRarity.SUPER_RARE:
      return 'bg-[#0087fa]';
    case BrawlerRarity.EPIC:
      return 'bg-[#AA15E4]';
    case BrawlerRarity.MYTHIC:
      return 'bg-[#EA3330]';
    case BrawlerRarity.LEGENDARY:
      return 'bg-[#FDF255]';
  }
}