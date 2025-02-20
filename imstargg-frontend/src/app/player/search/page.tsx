"use client";

import React, {useEffect, useState} from "react";
import {Player} from "@/model/Player";
import {useSearchParams} from "next/navigation";
import {PlayerSearchForm} from "@/components/player-search-form";
import {searchPlayer} from "@/lib/api/player";
import {PageHeader} from "@/components/page-header";
import {SearchedPlayer} from "@/app/player/search/_components/searched-player";
import Head from "next/head";
import Loading from "@/app/loading";
import gusSadPinSrc from "@/../public/icon/brawler/gus/gus_sad_pin.png";
import Image from "next/image";


export default function PlayerSearchResultPage() {
  const searchParams = useSearchParams();
  const query = searchParams.get('q');
  const [players, setPlayers] = useState<Player[] | null>(null);

  useEffect(() => {
    console.log('플레이어 검색 결과 페이지 로드...query:', query);
    setPlayers(null);

    const fetchPlayers = async () => {
      console.log(`검색어: ${query}`);
      if (query === null) {
        setPlayers([]);
        return;
      }

      const results = await searchPlayer(query);
      setPlayers(results);
    };

    fetchPlayers();
  }, [query]);


  return (
    <>
      <Head>
        <title>{query} 검색결과</title>
      </Head>
      <div className="space-y-2">
        <PageHeader>
          <div className="w-full max-w-xl m-1">
            <PlayerSearchForm/>
          </div>
        </PageHeader>

        {players === null ? (
          <Loading/>
        ) : players.length === 0 ? (
          <NoPlayerResult query={query}/>
        ) : (
          <>
            <h2 className="text-xl font-bold mb-4">
              <QueryTitle query={query}/> 검색 결과
            </h2>
            <div className="flex flex-col gap-1">
              {players.map((player) => (
                <SearchedPlayer key={player.tag} player={player}/>
              ))}
            </div>
          </>
        )}
      </div>
    </>
  );
}

function NoPlayerResult({query}: Readonly<{ query: string | null }>) {
  return (
    <div className="flex flex-col gap-2 items-center justify-center text-center p-4">
      <h1 className="text-xl font-bold">
        <QueryTitle query={query}/> 검색 결과가 없습니다.
      </h1>
      <p>검색어를 다시 한번 확인해주세요.</p>
      <p>이름으로 검색해서 결과가 나오지 않는다면, 태그로 검색해주세요.</p>
      <Image
        src={gusSadPinSrc}
        alt="player-not-found-image"
      />
    </div>
  );
}

const QueryTitle = ({query}: Readonly<{ query: string | null }>) => {
  return (
    <>
      &#39;<span className="font-bold">{query !== null ? query : '(이름 없음)'}</span>&#39; 검색 결과
    </>
  );
}