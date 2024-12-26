export const BrawlerRarity = {
  STARTING_BRAWLER: 'STARTING_BRAWLER',
  RARE: 'RARE',
  SUPER_RARE: 'SUPER_RARE',
  EPIC: 'EPIC',
  MYTHIC: 'MYTHIC',
  LEGENDARY: 'LEGENDARY',
  TIME_LIMITED_BRAWLER: 'TIME_LIMITED_BRAWLER',
} as const;

export type BrawlerRarityType = typeof BrawlerRarity[keyof typeof BrawlerRarity];

export const BrawlerRarityValues = Object.keys(BrawlerRarity) as BrawlerRarityType[];