'use client';

import {PlayerBattle as IPlayerBattle} from "@/model/PlayerBattle";
import 'dayjs/locale/ko';
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";

dayjs.locale('ko');
dayjs.extend(relativeTime)

interface PlayerBattleProps {
  battle: IPlayerBattle;
}

export default function PlayerBattle({battle}: PlayerBattleProps) {
  return (
    <div
      className={`flex items-center p-4 rounded-lg ${
        battle.result === 'VICTORY' ? 'bg-emerald-100' :
          battle.result === 'DEFEAT' ? 'bg-red-100' : 'bg-gray-100'
      }`}
    >
      <div className="flex-1">
        <div className="flex items-center gap-2">
          <div className="w-12 h-12 bg-gray-200 rounded-full flex items-center justify-center text-sm">
            브롤러 이름
          </div>
          <div>
            <div className="text-sm text-gray-600">
              {dayjs(battle.battleTime).fromNow()}
            </div>
            <div className="font-medium">
              {battle.event?.map?.name || battle.type}
            </div>
            <div className="text-sm text-gray-500">
              {battle.event?.mode}
            </div>
          </div>
        </div>
      </div>
      <div className="text-right">
        {battle.duration && (
          <div className="text-lg font-bold">
            {battle.duration} 초
          </div>
        )}
        {battle.rank && (
          <div className="text-lg font-bold">
            {battle.rank}등
          </div>
        )}
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