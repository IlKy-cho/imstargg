"use client";

import React, {useEffect, useState} from "react";
import {Player} from "@/model/Player";
import {useRouter, useSearchParams} from "next/navigation";
import SearchedPlayer from "@/components/searched-player";
import PlayerSearchForm from "@/components/player-search-form";
import {searchPlayer} from "@/lib/api/player";
import {metadataTitle, playerHref} from "@/config/site";
import {useRecentSearches} from "@/hooks/useRecentSearchs";

export default function PlayerSearchResultPage() {
  const [players, setPlayers] = useState<Player[] | null>(null);
  const searchParams = useSearchParams();
  const {addSearchTerm} = useRecentSearches();
  const query = searchParams.get('q');
  const router = useRouter();

  useEffect(() => {
    document.title = metadataTitle(`${query || '플레이어'} 검색결과`);

    const fetchPlayers = async () => {
      if (query === null) {
        setPlayers([]);
        return;
      }

      try {
        console.log(`검색어: ${query}`);
        const results = await searchPlayer(query);
        setPlayers(results);
      } catch (error) {
        console.error('검색 결과를 불러오는 중 오류가 발생했습니다:', error);
        setPlayers([]);
      }
    };

    fetchPlayers();
  }, [query]);

  useEffect(() => {
    if (!players || players.length === 0) return;

    if (players.length === 1) {
      const player = players[0];
      addSearchTerm({type: 'player', value: {name: player.name, tag: player.tag}});
      router.replace(playerHref(player.tag));
    } else {
      if (query) {
        addSearchTerm({type: 'query', value: query});
      }
    }
  }, [players, query]);

  return (
    <div className="w-full max-w-xl mx-auto p-1">
      <div className="w-full max-w-xl m-1">
        <PlayerSearchForm/>
      </div>

      {players === null ? (
        <div className="w-full text-center py-8">검색 중...</div>
      ) : players.length === 0 ? (
        <div className="w-full text-center py-8">
          <>&#39;<span className="font-bold">{query || '(이름 없음)'}</span>&#39;의 검색 결과가 없습니다.</>
        </div>
      ) : (
        <>
          <h2 className="text-xl font-bold mb-4">
            &#39;<span className="font-bold">{query || '(이름 없음)'}</span>&#39; 검색 결과
          </h2>
          <div className="space-y-2">
            {players.map((player) => (
              <SearchedPlayer key={player.tag} player={player}/>
            ))}
          </div>
        </>
      )}
    </div>
  );
}