'use client';

import 'dayjs/locale/ko';
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
import { BattleResultValue } from "@/model/enums/BattleResult";
import { BattleType } from "@/model/enums/BattleType";
import Image from "next/image";
import { Separator } from "@/components/ui/separator";
import { BattlePlayer } from "@/model/BattlePlayer";
import BrawlerProfileImage from "@/components/brawler-profile-image";
import Link from "next/link";
import { cn } from "@/lib/utils";
import BattleEventMapImage from "@/components/battle-event-map-image";
import SoloRankTier from "@/components/solo-rank-tier";
import Trophy from "@/components/trophy";
import { BrawlerLink } from "@/components/brawler-link";
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "./ui/tooltip";
import { PowerLevel } from "./brawler";
import { useEffect, useState } from "react";
import { PlayerBattle as PlayerBattleModel } from "@/model/PlayerBattle";
import { getBattles } from "@/lib/api/battle";
import { Brawler, BrawlerCollection } from "@/model/Brawler";
import { LoadingButton } from "@/components/ui/expansion/loading-button";
import { battleTypeIconSrc, battleTypeTitle } from "@/lib/battle-type";
import { battleResultTitle } from "@/lib/battle-result";
import { playerBattleIconSrc, playerBattleModeTitle } from "@/lib/player-battle";
import { battleEventHref, playerHref } from "@/config/site";
import { Button } from './ui/button';

dayjs.locale('ko');
dayjs.extend(relativeTime);

type PlayerBattleListProps = {
  tag: string;
  brawlerList: Brawler[];
};

export function PlayerBattleList({ tag, brawlerList }: Readonly<PlayerBattleListProps>) {
  const [battles, setBattles] = useState<PlayerBattleModel[]>([]);
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
  });

  return (
    <div className="flex flex-col gap-2">
      {battles.map((battle, index) => (
        <PlayerBattle
          key={index}
          battle={battle}
          brawlers={brawlerList}
          myTag={tag}
        />
      ))}
      <LoadingButton loading={loading} disabled={!hasMore} onClick={fetchBattles} variant='outline'>
        {hasMore ? '더보기' : '더 이상 배틀 기록이 없습니다.'}
      </LoadingButton>
    </div>
  );
}

type PlayerBattleProps = {
  battle: PlayerBattleModel;
  brawlers: Brawler[];
  myTag: string;
}

export default function PlayerBattle({ battle, brawlers, myTag }: Readonly<PlayerBattleProps>) {
  return (
    <div className={`flex flex-col rounded-lg overflow-hidden ${battleBackgroundColor(battle)} ${battleBorderColor(battle)}`}>
      <PlayerBattleHeader battle={battle} />
      <div className="flex flex-1 p-1 sm:p-2">
        <div className="flex items-center justify-center w-20 sm:w-28">
          <BattleEventLink battle={battle}>
            <BattleEventMapImage battleEventMap={battle.event.map} />
          </BattleEventLink>
        </div>
        <Separator className="m-1" orientation="vertical" />
        <div className="flex items-center justify-center flex-1">
          <BattleTeams battle={battle} myTag={myTag} brawlers={brawlers} />
        </div>
      </div>
    </div>
  );
}

function battleBackgroundColor(battle: PlayerBattleModel) {
  if (battle.result) {
    switch (battle.result) {
      case BattleResultValue.VICTORY:
        return 'bg-blue-100/50';
      case BattleResultValue.DEFEAT:
        return 'bg-red-100/50';
      case BattleResultValue.DRAW:
        return 'bg-amber-100/50';
    }
  }

  return 'bg-zinc-100/50';
}

function battleBorderColor(battle: PlayerBattleModel) {
  if (battle.result) {
    switch (battle.result) {
      case BattleResultValue.VICTORY:
        return 'border-l-4 border-blue-500';
      case BattleResultValue.DEFEAT:
        return 'border-l-4 border-red-500';
      case BattleResultValue.DRAW:
        return 'border-l-4 border-amber-500';
    }
  }
  return 'border-l-4 border-zinc-300';
}

function PlayerBattleHeader({ battle }: { battle: PlayerBattleModel }) {
  const modeTitle = playerBattleModeTitle(battle);
  const mapName = battle.event ? battle.event.map.name : '❓';

  return (
    <div className={`flex items-center p-1 w-full ${battleHeaderBackgroundColor(battle)}`}>
      <div className="flex items-center gap-1">
        <BattleTypeIcon type={battle.type} />
        {battleTypeTitle(battle.type) && (
          <span className="font-bold text-sm sm:text-base">{battleTypeTitle(battle.type)}</span>
        )}
        <Separator orientation="vertical" />
        <TooltipProvider>
          <Tooltip>
            <TooltipTrigger>
              <div className="text-xs sm:text-sm text-zinc-600">
                {dayjs(battle.battleTime).fromNow()}
              </div>
            </TooltipTrigger>
            <TooltipContent>
              {dayjs(battle.battleTime).format('YYYY-MM-DD HH:mm')}
            </TooltipContent>
          </Tooltip>
        </TooltipProvider>
        <Separator orientation="vertical" />
        <BattleResultInfo battle={battle} />
        <div className="text-xs sm:text-sm text-zinc-600">
          {battle.duration && (
            <span>{battle.duration}초</span>
          )}
        </div>
        <Separator orientation="vertical" />
        <div className="flex items-center gap-1">
          <BattleModeIcon battle={battle} />
          <span className="font-bold text-sm sm:text-base">{modeTitle}</span>
        </div>
        <div className="text-xs sm:text-sm text-zinc-600">
          <span>{mapName}</span>
        </div>
      </div>
    </div>
  );
}

function BattleEventLink({ battle, children }: { battle: PlayerBattleModel, children: React.ReactNode }) {
  if (!battle.event.id) {
    return null;
  }

  return (
    <Link href={battleEventHref(battle.event.id)}>
      {children}
    </Link>
  );
}

function battleHeaderBackgroundColor(battle: PlayerBattleModel) {
  if (battle.result) {
    switch (battle.result) {
      case BattleResultValue.VICTORY:
        return 'bg-blue-100';
      case BattleResultValue.DEFEAT:
        return 'bg-red-100';
      case BattleResultValue.DRAW:
        return 'bg-amber-100';
    }
  }

  return 'bg-zinc-100';
}

function BattleResultInfo({ battle }: { battle: PlayerBattleModel }) {
  if (battle.result) {
    return (
      <div className='font-bold text-sm sm:text-base'>
        {battleResultTitle(battle.result)}
      </div>
    );
  } else if (battle.rank) {
    return (
      <div className='font-bold text-sm sm:text-base'>
        {battle.rank}위
      </div>
    );
  }

  return null;
}

function BattleTypeIcon({ type }: { type: BattleType }) {
  const iconSrc = battleTypeIconSrc(type);
  if (!iconSrc) {
    return null;
  }

  return (
    <Image
      src={iconSrc}
      alt="battle type icon"
      width={24}
      height={24}
    />
  );
}

function BattleModeIcon({ battle }: { battle: PlayerBattleModel }) {
  const iconSrc = playerBattleIconSrc(battle);
  if (!iconSrc) {
    return null;
  }

  return (
    <Image src={iconSrc} alt="battle event icon" width={24} height={24} />
  );
}


function BattleTeams(
  { battle, myTag, brawlers }: { battle: PlayerBattleModel, myTag: string, brawlers: Brawler[] }
) {
  const brawlerCollection = new BrawlerCollection(brawlers);
  return (
    <div className="flex flex-col gap-1 items-center justify-center">
      {battle.teams.map((team, i) => (
        <div key={i} className="flex items-center gap-2">
          {team.map((player, j) => (
            <BattleTeamPlayer
              key={j}
              player={player}
              myTag={myTag}
              starPlayerTag={battle.starPlayerTag}
              brawler={brawlerCollection.find(player.brawler.id)}
            />
          ))}
        </div>
      ))}
    </div>
  );
}

function BattleTeam({ team, myTag, starPlayerTag, brawlers }: { team: BattlePlayer[], myTag: string, starPlayerTag: string | null, brawlers: Brawler[] }) {
  const brawlerCollection = new BrawlerCollection(brawlers);
  return (
    <div className="flex items-center gap-2">
      {team.map((player, j) => (
        <BattleTeamPlayer
          key={j}
          player={player}
          myTag={myTag}
          starPlayerTag={starPlayerTag}
          brawler={brawlerCollection.find(player.brawler.id)}
        />
      ))}
    </div>
  );
}

function BattleTeamPlayer(
  { player, brawler, myTag, starPlayerTag }: {
    player: BattlePlayer,
    brawler: Brawler | null,
    myTag: string,
    starPlayerTag: string | null
  }
) {
  const nameStyle = player.tag === myTag
    ? 'font-bold text-zinc-800' : 'text-zinc-500';

  return (
    <div className="flex flex-col items-center w-20">
      <BrawlerLink brawler={brawler}>
        <div className="relative">
          <div className="absolute top-0 left-0 z-10 bg-zinc-200/50">
            <PowerLevel value={player.brawler.power} />
          </div>
          <div className="absolute bottom-0 right-0 z-10 bg-zinc-200/50">
            <PlayerTier player={player} />
          </div>

          <BrawlerProfileImage brawler={brawler} />
        </div>
      </BrawlerLink>
      <div className="w-full">
        <div className="flex gap-1">
          <Link
            href={playerHref(player.tag)}
            className={cn(nameStyle, 'text-xs', 'flex-1', 'truncate')}
          >
            {player.name}
          </Link>
          {starPlayerTag === player.tag && (
            <span className="text-xs">⭐️</span>
          )}
        </div>
      </div>
    </div>
  );
}

export function PlayerTier({ player }: { player: BattlePlayer }) {
  if (player.soloRankTier) {
    return (
      <div className="flex items-center gap-1">
        <SoloRankTier tier={player.soloRankTier} />
      </div>
    );
  } else if (player.brawler.trophies) {
    return (
      <div className="flex items-center gap-1">
        <Trophy value={player.brawler.trophies} />
      </div>
    );
  }

  return null;
}