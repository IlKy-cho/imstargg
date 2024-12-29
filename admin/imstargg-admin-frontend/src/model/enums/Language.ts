export const LanguageValue = {
  KOREAN: 'KOREAN',
  ENGLISH: 'ENGLISH',
} as const;

export type Language = typeof LanguageValue[keyof typeof LanguageValue];

export const LanguageValues = Object.keys(LanguageValue) as Language[];