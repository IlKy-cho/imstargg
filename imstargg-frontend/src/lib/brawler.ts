import {BrawlerRarityValue} from "@/model/enums/BrawlerRarity";
import {Brawler} from "@/model/Brawler";

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
