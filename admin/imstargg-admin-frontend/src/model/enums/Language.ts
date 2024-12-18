export const Language = {
  KOREAN: 'KOREAN',
  ENGLISH: 'ENGLISH',
} as const;

export type LanguageType = typeof Language[keyof typeof Language];

export const LanguageValues = Object.keys(Language) as LanguageType[];
