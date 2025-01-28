export const CountryValue = {
  GLOBAL: 'global',
  KOREA: 'kr',
} as const;

export type Country = typeof CountryValue[keyof typeof CountryValue];

export const CountryValues = Object.keys(CountryValue) as Country[];
