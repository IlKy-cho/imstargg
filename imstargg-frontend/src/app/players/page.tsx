import {Metadata} from "next";
import PlayerSearchForm from "@/components/player-search-form";
import {Ranking} from "@/components/ranking";
import {getPlayerRanking} from "@/lib/api/ranking";
import {Country} from "@/model/enums/Country";
import {countryOrDefault} from "@/lib/country";

export const metadata: Metadata = {
  title: '플레이어',
  description: '브롤스타즈의 플레이어 순위를 확인해보고, 플레이어를 검색해보세요.',
}

interface SearchParams {
  country?: Country;
}

interface PageProps {
  searchParams: Promise<SearchParams>;
}

export default async function PlayersPage({searchParams}: Readonly<PageProps>) {

  const country = countryOrDefault((await searchParams).country);

  const rankings = await getPlayerRanking(country);

  return (
    <div className="space-y-2">
      <div className="flex gap-4 bg-cover bg-center bg-no-repeat bg-brawl-stars-lobby p-4">
        <div className="flex-1 m-2">
          <PlayerSearchForm/>
        </div>
      </div>
      <Ranking rankings={rankings} country={country} size="screen"/>
    </div>
  );
};