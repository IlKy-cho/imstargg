import {BrawlerRarity, BrawlerRarityValue} from "@/model/enums/BrawlerRarity";

export const brawlerRarityTitle = (rarity: BrawlerRarity) => {
  switch(rarity) {
    case BrawlerRarityValue.STARTING_BRAWLER:
      return '기본 브롤러';
    case BrawlerRarityValue.RARE:
      return '희귀';
    case BrawlerRarityValue.SUPER_RARE:
      return '초희귀';
    case BrawlerRarityValue.EPIC:
      return '영웅';
    case BrawlerRarityValue.MYTHIC:
      return '신화';
    case BrawlerRarityValue.TIME_LIMITED_BRAWLER:
      return '기간 한정 브롤러';
    case BrawlerRarityValue.LEGENDARY:
      return '전설';
  }
}