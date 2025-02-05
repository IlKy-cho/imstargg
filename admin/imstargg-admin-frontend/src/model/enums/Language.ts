export const LanguageValue = {
  ENGLISH: 'ENGLISH',
  KOREAN: 'KOREAN',
} as const;

export type Language = typeof LanguageValue[keyof typeof LanguageValue];

export const LanguageValues = Object.keys(LanguageValue) as Language[];

const languageToOrder: Record<Language, number> = Object.keys(LanguageValue).reduce(
  (acc, key, index) => ({
    ...acc,
    [key]: index + 1,
  }),
  {} as Record<Language, number>
);

export const languageOrder = (language: Language) => languageToOrder[language];
