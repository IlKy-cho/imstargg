'use client';

import {getBattles} from "@/app/players/[tag]/_lib/getBattles";
import {useEffect, useState} from "react";
import PlayerBattle from "@/model/PlayerBattle";
import PlayerBattle from "./PlayerBattle";

type Props = {
  tag: string;
};

export default function PlayerBattleList({ tag }: Readonly<Props>) {
  const [battles, setBattles] = useState<PlayerBattle[]>([]);
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);

  const fetchBattles = async () => {
    if (loading || !hasMore) return;
    
    setLoading(true);
    try {
      const newBattles = await getBattles(tag, page);
      if (newBattles.length === 0) {
        setHasMore(false);
      } else {
        setBattles(prev => [...prev, ...newBattles]);
        setPage(prev => prev + 1);
      }
    } catch (error) {
      console.error('Failed to fetch battles:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBattles();
  }, []);

  return (
    <div className="flex flex-col gap-2 p-4">
      {battles.map((battle, index) => (
        <PlayerBattle
          key={`${battle.battleTime}-${index}`}
          battle={battle}
        />
      ))}
      {loading && <div className="text-center p-4">로딩 중...</div>}
      {!loading && hasMore && (
        <button
          onClick={fetchBattles}
          className="mt-4 px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors"
        >
          더 보기
        </button>
      )}
      {!hasMore && <div className="text-center p-4">더 이상 배틀 기록이 없습니다.</div>}
    </div>
  );
}
