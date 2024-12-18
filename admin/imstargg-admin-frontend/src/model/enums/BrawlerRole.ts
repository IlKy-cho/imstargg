const BrawlerRole = {
  TANK: 'TANK',
  ASSASSIN: 'ASSASSIN',
  SUPPORT: 'SUPPORT',
  CONTROLLER: 'CONTROLLER',
  DAMAGE_DEALER: 'DAMAGE_DEALER',
  MARKSMAN: 'MARKSMAN',
  ARTILLERY: 'ARTILLERY',
} as const;

export type BrawlerRole = typeof BrawlerRole[keyof typeof BrawlerRole];