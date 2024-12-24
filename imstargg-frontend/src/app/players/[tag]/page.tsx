import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import PlayerBattleList from './_components/PlayerBattleList';
import {getPlayer} from "@/lib/api/getPlayer";
import React from "react";
import PlayerProfile from "@/components/player-profile";
import PlayerSearchForm from '@/components/player-search-form';

dayjs.locale('ko');
dayjs.extend(relativeTime);

type PlayerNotFoundProps = {
  tag: string;
}

function PlayerNotFound({tag}: Readonly<PlayerNotFoundProps>) {
  return (
    <div className="space-y-10 my-10">
      <div className="text-center space-y-2">
        <h1 className="text-2xl font-bold">태그 {decodeURIComponent(tag)} 플레이어를 찾을 수 없습니다</h1>
        <p className="text-gray-500">다른 플레이어를 검색해보세요</p>
      </div>
      
      <div className="flex justify-center">
        <PlayerSearchForm />
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
  const playerResponse = await getPlayer(params.tag);

  if (!playerResponse.player) {
    return <PlayerNotFound tag={params.tag}/>;
  }
  const player = playerResponse.player;

  return (
    <div className="space-y-4">
      <PlayerProfile player={player}/>
      <PlayerBattleList tag={params.tag}/>
    </div>
  );
}
