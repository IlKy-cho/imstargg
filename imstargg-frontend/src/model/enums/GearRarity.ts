export const GearRarityValue = {
  SUPER_RARE: 'SUPER_RARE',
  EPIC: 'EPIC',
  MYTHIC: 'MYTHIC',
} as const;

export type GearRarity = typeof GearRarityValue[keyof typeof GearRarityValue];

export const GearRarityValues = Object.keys(GearRarityValue) as GearRarity[];