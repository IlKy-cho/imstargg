import React from "react";
import PlayerProfile from "@/components/player-profile";
import {getBrawlers} from "@/lib/api/brawler";
import {getPlayer, getPlayerBrawlers} from "@/lib/api/player";
import {notFound} from "next/navigation";
import {Metadata} from "next";
import {PlayerBattleContent} from "@/components/player-battle";
import {Brawler} from "@/model/Brawler";
import {PlayerBrawlerList} from "@/components/player-brawler";
import {PageHeader} from "@/components/page-header";


type Props = {
  params: Promise<{ tag: string; }>
};

export async function generateMetadata({params}: Readonly<Props>): Promise<Metadata> {
  const {tag} = await params;
  const decodedTag = decodeURIComponent(tag);
  const player = await getPlayer(decodedTag);
  if (!player) {
    notFound();
  }
  return {
    title: `${player.name}${player.tag}`,
    description: `브롤스타즈 플레이어 ${player.name}${player.tag}의 정보, 전적, 통계를 확인해보세요.`,
  }
}

export default async function PlayerPage({params}: Readonly<Props>) {
  const { tag } = await params;
  const decodedTag = decodeURIComponent(tag);
  const player = await getPlayer(decodedTag);

  if (!player) {
    notFound();
  }

  const brawlers = await getBrawlers();

  return (
    <div className="space-y-2">
      <PageHeader>
        <PlayerProfile player={player}/>
      </PageHeader>
      <PagePlayerBrawlerList tag={decodedTag} brawlers={brawlers}/>
      <PlayerBattleContent player={player} brawlers={brawlers}/>
    </div>
  );
}

async function PagePlayerBrawlerList({tag, brawlers}: Readonly<{tag: string, brawlers: Brawler[]}>) {
  const playerBrawlers = await getPlayerBrawlers(tag);

  return (
    <PlayerBrawlerList brawlers={brawlers} playerBrawlers={playerBrawlers} />
  );
}