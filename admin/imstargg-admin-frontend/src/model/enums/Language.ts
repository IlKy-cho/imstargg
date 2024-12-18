const Language = {
  KOREAN: 'KOREAN',
  ENGLISH: 'ENGLISH',
} as const;

export type Language = typeof Language[keyof typeof Language];