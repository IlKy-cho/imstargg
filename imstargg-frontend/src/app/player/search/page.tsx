"use client";

import React, {useEffect, useState} from "react";
import {Player} from "@/model/Player";
import {useSearchParams} from "next/navigation";
import {PlayerSearchForm} from "@/components/player-search-form";
import {searchPlayer} from "@/lib/api/player";
import {useRecentSearches} from "@/hooks/useRecentSearchs";
import {PageHeader} from "@/components/page-header";
import {SearchedPlayer} from "@/app/player/search/_components/searched-player";

export default function PlayerSearchResultPage() {
  const searchParams = useSearchParams();
  const query = searchParams.get('q');
  const [loading, setLoading] = useState(true);
  const [players, setPlayers] = useState<Player[] | null>(null);
  const {addSearchTerm} = useRecentSearches();

  useEffect(() => {
    setLoading(true);
    const fetchPlayers = async () => {
      if (query === null) {
        setPlayers([]);
        setLoading(false);
        return;
      }

      try {
        console.log(`검색어: ${query}`);
        const results = await searchPlayer(query);
        setPlayers(results);
      } catch (error) {
        console.error('검색 결과를 불러오는 중 오류가 발생했습니다:', error);
        setPlayers([]);
      } finally {
        setLoading(false);
      }
    };

    fetchPlayers();
  }, [query]);

  useEffect(() => {
    if (!players || players.length === 0) return;
    if (query) {
      addSearchTerm({type: 'query', value: query});
    }
  }, [players, query]);

  return (
    <div className="space-y-2">
      <PageHeader>
        <div className="w-full max-w-xl m-1">
          <PlayerSearchForm/>
        </div>
      </PageHeader>

      <div>
        {loading ? (
          <div className="w-full text-center py-8">검색 중...</div>
        ) : players === null || players.length === 0 ? (
          <div className="w-full text-center py-8">
            <>&#39;<span className="font-bold">{query || '(이름 없음)'}</span>&#39;의 검색 결과가 없습니다.</>
          </div>
        ) : (
          <>
            <h2 className="text-xl font-bold mb-4">
              &#39;<span className="font-bold">{query || '(이름 없음)'}</span>&#39; 검색 결과
            </h2>
            <div className="flex flex-col gap-1">
              {players.map((player) => (
                <SearchedPlayer key={player.tag} player={player}/>
              ))}
            </div>
          </>
        )}
      </div>
    </div>
  );
}