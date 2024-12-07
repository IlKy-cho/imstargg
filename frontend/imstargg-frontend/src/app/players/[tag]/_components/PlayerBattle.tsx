'use client';

import Battle from "@/model/Battle";
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";

// relativeTime 플러그인 등록
dayjs.extend(relativeTime);

interface PlayerBattleProps {
  battle: Battle;
}

export default function PlayerBattle({battle}: PlayerBattleProps) {
  return (
    <div
      className={`flex items-center p-4 rounded-lg ${
        battle.result === 'victory' ? 'bg-emerald-100' :
          battle.result === 'defeat' ? 'bg-red-100' : 'bg-gray-100'
      }`}
    >
      <div className="flex-1">
        <div className="flex items-center gap-2">
          <div className="w-12 h-12 bg-gray-200 rounded-full flex items-center justify-center text-sm">
            {battle.teams[0][0].brawler}
          </div>
          <div>
            <div className="text-sm text-gray-600">
              {dayjs(battle.battleTime).fromNow(true)}
            </div>
            <div className="font-medium">
              {battle.event}
            </div>
          </div>
        </div>
      </div>
      <div className="text-right">
        <div className="text-lg font-bold">
          {battle.duration} seconds
        </div>
        <div className="text-sm">
          {battle.trophyChange != null && (
            <>
              {battle.trophyChange > 0 ? '+' : ''}{battle.trophyChange} 트로피
            </>
          )}
        </div>
      </div>
    </div>
  );
} 