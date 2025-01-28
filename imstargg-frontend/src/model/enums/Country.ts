export const CountryValue = {
  GLOBAL: 'GLOBAL',
  KOREA: 'KOREA',
} as const;

export type Country = typeof CountryValue[keyof typeof CountryValue];

export const CountryValues = Object.keys(CountryValue) as Country[];
