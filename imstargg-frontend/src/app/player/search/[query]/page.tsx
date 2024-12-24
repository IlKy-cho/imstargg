"use client";

import React, {useEffect, useState} from "react";
import {Player} from "@/model/Player";
import {searchPlayer} from "@/lib/api/searchPlayer";
import {useParams, useRouter} from "next/navigation";
import SearchedPlayer from "@/components/searched-player";
import PlayerSearchForm from "@/components/player-search-form";

export default function SearchPage() {
  const [isLoading, setIsLoading] = useState(true);
  const [players, setPlayers] = useState<Player[]>([]);
  const params = useParams<{ query: string; }>();
  const router = useRouter();

  useEffect(() => {
    const query = decodeURIComponent(params.query);
    document.title = `${query}의 검색결과 - ImStarGG`;

    const fetchPlayers = async () => {
      try {
        console.log(`검색어: ${query}`);
        const results = await searchPlayer(query);
        if (results.length === 1) {
          router.push(`/players/${encodeURIComponent(results[0].tag)}`);
          return;
        }

        setPlayers(results);
      } catch (error) {
        console.error('검색 결과를 불러오는 중 오류가 발생했습니다:', error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchPlayers();
  }, [params.query, router]);

  if (isLoading) {
    return <div>로딩 중...</div>;
  }

  return (
    <div className="w-full max-w-xl mx-auto">
      <PlayerSearchForm/>
      {players.length === 0 ? (
        <div className="w-full max-w-xl mx-auto text-center py-8">
          검색 결과가 없습니다.
        </div>
      ) : (<>
        <h2 className="text-xl font-bold mb-4">검색 결과</h2>
        <div className="space-y-2">
          {players.map((player) => (
            <SearchedPlayer key={player.tag} player={player}/>
          ))}
        </div>
      </>)}
    </div>
  );
} 