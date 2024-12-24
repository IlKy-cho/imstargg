export const ApiErrorType = {
  PLAYER_NOT_FOUND: 'PLAYER_NOT_FOUND',
} as const;

export type ApiErrorTypeType = typeof ApiErrorType[keyof typeof ApiErrorType];
