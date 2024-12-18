export const BrawlerRarity = {
  STARTING_BRAWLER: 'STARTING_BRAWLER',
  RARE: 'RARE',
  SUPER_RARE: 'SUPER_RARE',
  EPIC: 'EPIC',
  MYTHIC: 'MYTHIC',
  LEGENDARY: 'LEGENDARY',

  values() {
    return Object.keys(this) as BrawlerRarityType[];
  }
} as const;

export type BrawlerRarityType = typeof BrawlerRarity[keyof typeof BrawlerRarity];
