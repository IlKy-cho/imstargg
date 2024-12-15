const GearRarity = {
  SUPER_RARE: 'SUPER_RARE',
  EPIC: 'EPIC',
  MYTHIC: 'MYTHIC',
} as const;

export type GearRarity = typeof GearRarity[keyof typeof GearRarity];