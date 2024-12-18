const Language = {
  KOREAN: 'KOREAN',
  ENGLISH: 'ENGLISH',
} as const;

export type Language = typeof Language[keyof typeof Language];

export const LanguageValues = Object.keys(Language) as Language[];
