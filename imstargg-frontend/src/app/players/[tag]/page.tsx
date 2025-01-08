import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import React from "react";
import PlayerProfile from "@/components/player-profile";
import PlayerSearchForm from '@/components/player-search-form';
import PlayerBattleList from "@/components/player-battle-list";
import {getBrawlers} from "@/lib/api/brawler";
import {getPlayer} from "@/lib/api/player";
import {notFound} from "next/navigation";

dayjs.locale('ko');
dayjs.extend(relativeTime);


type Props = {
  params: {
    tag: string;
  }
};

export default async function PlayerPage({params}: Readonly<Props>) {
  const { tag } = await params;
  const decodedTag = decodeURIComponent(tag);
  const playerResponse = await getPlayer(decodedTag);

  if (!playerResponse.player) {
    notFound();
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
