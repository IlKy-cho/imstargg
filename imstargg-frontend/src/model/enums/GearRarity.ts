export const GearRarity = {
  SUPER_RARE: 'SUPER_RARE',
  EPIC: 'EPIC',
  MYTHIC: 'MYTHIC',
} as const;

export type GearRarityType = typeof GearRarity[keyof typeof GearRarity];

export const GearRarityValues = Object.keys(GearRarity) as GearRarityType[];