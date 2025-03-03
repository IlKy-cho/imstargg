import React from "react";
import {getBrawlers} from "@/lib/api/brawler";
import {getPlayer, getPlayerBrawlers} from "@/lib/api/player";
import {notFound} from "next/navigation";
import {Metadata} from "next";
import {PlayerBattleContent} from "@/components/player-battle";
import {Brawler} from "@/model/Brawler";
import {PlayerBrawlerList} from "@/components/player-brawler";
import {PageHeader, pageHeaderContainerDefault} from "@/components/page-header";
import {Player} from "@/model/Player";
import Trophy from "@/components/trophy";
import SoloRankTier from "@/components/solo-rank-tier";
import dayjs from "dayjs";
import 'dayjs/locale/ko';
import relativeTime from "dayjs/plugin/relativeTime";
import PlayerRenewButton from "@/components/player-renew-button";
import {cn} from "@/lib/utils";
import PlayerRecentTracker from "@/components/player-recent-tracker";
import { processPlayerRenewal } from "@/lib/player";

dayjs.locale('ko');
dayjs.extend(relativeTime);

interface SearchParams {
  renewIfAbsent?: boolean;
}

type Props = {
  params: Promise<{ tag: string; }>;
  searchParams: Promise<SearchParams>;
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

export default async function PlayerPage({params, searchParams}: Readonly<Props>) {
  const { tag } = await params;
  const { renewIfAbsent } = await searchParams;
  const decodedTag = decodeURIComponent(tag);
  let player = await getPlayer(decodedTag);

  if (!player) {
    console.log(`player not found: ${decodedTag}`);
    if (renewIfAbsent) {
      console.log(`renewing player: ${decodedTag}`);
      await processPlayerRenewal(decodedTag);
      player = await getPlayer(decodedTag);
    }
    if (!player) {
      notFound();
    }
  }

  const brawlers = await getBrawlers();

  return (
    <>
      <PlayerRecentTracker player={player} />
      <div className="space-y-2">
        <PageHeader>
          <PlayerProfile player={player}/>
        </PageHeader>
        <PagePlayerBrawlerList tag={decodedTag} brawlers={brawlers}/>
        <PlayerBattleContent player={player} brawlers={brawlers}/>
      </div>
    </>
  );
}

async function PagePlayerBrawlerList({tag, brawlers}: Readonly<{tag: string, brawlers: Brawler[]}>) {
  const playerBrawlers = await getPlayerBrawlers(tag);

  return (
    <PlayerBrawlerList brawlers={brawlers} playerBrawlers={playerBrawlers} />
  );
}

const PlayerInfoContainer = ({label, children}: { label: string, children: React.ReactNode }) => (
  <div className="flex justify-between items-center text-sm sm:text-base">
    <span className="text-zinc-500">{label}</span>
    {children}
  </div>
);

function PlayerProfile({player}: Readonly<{ player: Player }>) {
  return (
    <div className={cn("space-y-1", pageHeaderContainerDefault)}>
      <div>
        <PlayerInfoContainer label="이름">
          <span>{player.name}</span>
        </PlayerInfoContainer>

        <PlayerInfoContainer label="태그">
          <span>{player.tag}</span>
        </PlayerInfoContainer>

        <PlayerInfoContainer label="클럽 태그">
          {player.clubTag ?
            <span>{player.clubTag}</span>
            : <span>❌</span>
          }
        </PlayerInfoContainer>

        <PlayerInfoContainer label="트로피">
          <Trophy value={player.trophies}/>
        </PlayerInfoContainer>

        <PlayerInfoContainer label="최고 트로피">
          <Trophy value={player.highestTrophies}/>
        </PlayerInfoContainer>

        <PlayerInfoContainer label="경쟁전">
          {player.soloRankTier ?
            <SoloRankTier tier={player.soloRankTier}/>
            : <span className="text-gray-400">❓</span>
          }
        </PlayerInfoContainer>

        <div className="text-xs sm:text-sm text-gray-400">
          마지막 업데이트: {dayjs(player.updatedAt).fromNow()}
        </div>
      </div>
      <PlayerRenewButton player={player}/>
    </div>
  );
}