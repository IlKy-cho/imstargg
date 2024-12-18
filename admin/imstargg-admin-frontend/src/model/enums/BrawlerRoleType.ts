export const BrawlerRole = {
  TANK: 'TANK',
  ASSASSIN: 'ASSASSIN',
  SUPPORT: 'SUPPORT',
  CONTROLLER: 'CONTROLLER',
  DAMAGE_DEALER: 'DAMAGE_DEALER',
  MARKSMAN: 'MARKSMAN',
  ARTILLERY: 'ARTILLERY',

} as const;

export type BrawlerRoleType = typeof BrawlerRole[keyof typeof BrawlerRole];

export const BrawlerRoleValues = Object.keys(BrawlerRole) as BrawlerRoleType[];
