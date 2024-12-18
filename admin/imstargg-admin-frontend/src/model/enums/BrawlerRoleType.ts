export const BrawlerRole = {
  TANK: 'TANK',
  ASSASSIN: 'ASSASSIN',
  SUPPORT: 'SUPPORT',
  CONTROLLER: 'CONTROLLER',
  DAMAGE_DEALER: 'DAMAGE_DEALER',
  MARKSMAN: 'MARKSMAN',
  ARTILLERY: 'ARTILLERY',

  values() {
    return Object.keys(this) as BrawlerRoleType[];
  }
} as const;

export type BrawlerRoleType = typeof BrawlerRole[keyof typeof BrawlerRole];
