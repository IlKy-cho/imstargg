"use client";

import {Country, CountryValues} from "@/model/enums/Country";
import {useRouter, useSearchParams} from "next/navigation";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {countryEmoji, countryTitle} from "@/lib/country";


export function CountrySelect({country} : {country: Country}) {
  const router = useRouter();
  const searchParams = useSearchParams();

  const handleCountryChange = (value: Country) => {
    const params = new URLSearchParams(searchParams);
    params.set('country', value);
    router.replace(`?${params.toString()}`);
  };

  return (
    <Select
      defaultValue={country}
      onValueChange={handleCountryChange}
    >
      <SelectTrigger className="w-28">
        <SelectValue placeholder="국가 선택"/>
      </SelectTrigger>
      <SelectContent>
        {CountryValues.map((country) => (
          <SelectItem key={country} value={country}>
            {countryContent(country)}
          </SelectItem>
        ))}
      </SelectContent>
    </Select>
  )
}

function countryContent(country: Country): string {
  return `${countryEmoji(country)} ${countryTitle(country)}`;
}