export const BrawlerRoleValue = {
  TANK: 'TANK',
  ASSASSIN: 'ASSASSIN',
  SUPPORT: 'SUPPORT',
  CONTROLLER: 'CONTROLLER',
  DAMAGE_DEALER: 'DAMAGE_DEALER',
  MARKSMAN: 'MARKSMAN',
  ARTILLERY: 'ARTILLERY',
  NONE: 'NONE',
} as const;

export type BrawlerRole = typeof BrawlerRoleValue[keyof typeof BrawlerRoleValue];

export const BrawlerRoleValues = Object.keys(BrawlerRoleValue) as BrawlerRole[];
