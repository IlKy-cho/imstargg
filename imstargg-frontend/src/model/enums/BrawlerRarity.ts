export const BrawlerRarityValue = {
  STARTING_BRAWLER: 'STARTING_BRAWLER',
  RARE: 'RARE',
  SUPER_RARE: 'SUPER_RARE',
  EPIC: 'EPIC',
  MYTHIC: 'MYTHIC',
  TIME_LIMITED_BRAWLER: 'TIME_LIMITED_BRAWLER',
  LEGENDARY: 'LEGENDARY',
} as const;

export type BrawlerRarity = typeof BrawlerRarityValue[keyof typeof BrawlerRarityValue];

export const BrawlerRarityValues = Object.keys(BrawlerRarityValue) as BrawlerRarity[];