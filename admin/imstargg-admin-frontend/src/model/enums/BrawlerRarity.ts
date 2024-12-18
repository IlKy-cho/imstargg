const BrawlerRarity = {
  STARTING_BRAWLER: 'STARTING_BRAWLER',
  RARE: 'RARE',
  SUPER_RARE: 'SUPER_RARE',
  EPIC: 'EPIC',
  MYTHIC: 'MYTHIC',
  LEGENDARY: 'LEGENDARY',
} as const;

export type BrawlerRarity = typeof BrawlerRarity[keyof typeof BrawlerRarity];