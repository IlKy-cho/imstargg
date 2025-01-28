import {Country, CountryValue} from "@/model/enums/Country";

export function countryTitle(country: Country): string {
  switch (country) {
    case CountryValue.GLOBAL:
      return '전세계';
    case CountryValue.KOREA:
      return '한국';
  }
}

export function countryEmoji(country: Country): string {
  switch (country) {
    case CountryValue.GLOBAL:
      return '🌍';
    case CountryValue.KOREA:
      return '🇰🇷';
  }
}

export function countryOrDefault(country?: Country): Country {
  return country ?? CountryValue.KOREA;
}