export const BattleMode = {
  NOT_FOUND: 'NOT_FOUND',
  KNOCKOUT: 'KNOCKOUT',
  GEM_GRAB: 'GEM_GRAB',
  HEIST: 'HEIST',
  HOT_ZONE: 'HOT_ZONE',
  SIEGE: 'SIEGE',
  BOUNTY: 'BOUNTY',
  BRAWL_BALL: 'BRAWL_BALL',
  DUELS: 'DUELS',
  SOLO_SHOWDOWN: 'SOLO_SHOWDOWN',
  DUO_SHOWDOWN: 'DUO_SHOWDOWN',
  WIPEOUT: 'WIPEOUT',
  VOLLEY_BRAWL: 'VOLLEY_BRAWL',
  TROPHY_THIEVES: 'TROPHY_THIEVES',
  BOSS_FIGHT: 'BOSS_FIGHT',
  BIG_GAME: 'BIG_GAME',
  BASKET_BRAWL: 'BASKET_BRAWL',
  ROBO_RUMBLE: 'ROBO_RUMBLE',
  PAYLOAD: 'PAYLOAD',
  TAKEDOWN: 'TAKEDOWN',
  HUNTERS: 'HUNTERS',
} as const;

export type BattleModeType = typeof BattleMode[keyof typeof BattleMode];

export const BattleModeValues = Object.keys(BattleMode) as BattleModeType[];