'use client';

import {useEffect, useState} from "react";
import {PlayerBattle as IPlayerBattle} from "@/model/PlayerBattle";
import {getBattles} from "@/lib/api/battle";
import PlayerBattle from "@/components/player-battle";
import {Brawler} from "@/model/Brawler";
import {LoadingButton} from "@/components/ui/expansion/loading-button";

type Props = {
  tag: string;
  brawlerList: Brawler[];
};

export default function PlayerBattleList({tag, brawlerList}: Readonly<Props>) {
  const [battles, setBattles] = useState<IPlayerBattle[]>([]);
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);

  const fetchBattles = async () => {
    if (loading || !hasMore) return;

    setLoading(true);
    try {
      const newBattleSlice = await getBattles(tag, page);
      setHasMore(newBattleSlice.hasNext);
      setBattles(prev => [...prev, ...newBattleSlice.content]);
      setPage(prev => prev + 1);
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
          key={index}
          battle={battle}
          brawlerList={brawlerList}
        />
      ))}
      <LoadingButton loading={loading} disabled={!hasMore} onClick={fetchBattles} variant='outline'>
        {hasMore ? '더보기' : '더 이상 배틀 기록이 없습니다.'}
      </LoadingButton>
    </div>
  );
}
