import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import React from "react";
import PlayerProfile from "@/components/player-profile";
import PlayerSearchForm from '@/components/player-search-form';
import PlayerBattleList from "@/components/player-battle-list";
import {getBrawlers} from "@/lib/api/brawler";
import {getPlayer} from "@/lib/api/player";

dayjs.locale('ko');
dayjs.extend(relativeTime);

type PlayerNotFoundProps = {
  tag: string;
}

function PlayerNotFound({tag}: Readonly<PlayerNotFoundProps>) {
  return (
    <div className="space-y-10 my-10">
      <div className="text-center space-y-2">
        <h1 className="text-2xl font-bold">태그 {tag} 플레이어를 찾을 수 없습니다</h1>
        <p className="text-gray-500">다른 플레이어를 검색해보세요</p>
      </div>

      <div className="flex justify-center">
        <PlayerSearchForm/>
      </div>
    </div>
  );
}

type Props = {
  params: {
    tag: string
  }
};

export default async function PlayerPage({params}: Readonly<Props>) {
  const { tag } = await params;
  const decodedTag = decodeURIComponent(tag);
  const playerResponse = await getPlayer(tag);

  if (!playerResponse.player) {
    return <PlayerNotFound tag={decodedTag}/>;
  }
  const player = playerResponse.player;
  const brawlers = await getBrawlers();

  return (
    <div className="space-y-2">
      <div className="flex flex-col lg:flex-row gap-4 bg-cover bg-center bg-no-repeat bg-brawl-stars-lobby p-4">
        <div className="flex-1">
          <PlayerProfile player={player}/>
        </div>
      </div>
      <PlayerBattleList tag={decodedTag} brawlerList={brawlers}/>
    </div>
  );
}
