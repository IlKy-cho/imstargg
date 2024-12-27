'use client';

import {PlayerBattle as IPlayerBattle} from "@/model/PlayerBattle";
import 'dayjs/locale/ko';
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
import {Brawler, BrawlersImpl} from "@/model/Brawler";
import {battleResultTitle, battleTypeTitle, playerBattleModeTitle} from "./title";
import {BattleResultValue} from "@/model/enums/BattleResult";
import {BattleType} from "@/model/enums/BattleType";
import {battleTypeIconSrc, playerBattleIconSrc} from "@/components/icon";
import Image from "next/image";
import {Separator} from "@/components/ui/separator";
import {BattlePlayer} from "@/model/BattlePlayer";
import BrawlerProfileImage from "@/components/brawler-profile-image";
import Link from "next/link";
import {cn} from "@/lib/utils";
import BattleEventMapImage from "@/components/battle-event-map-image";
import SoloRankTier from "@/components/solo-rank-tier";
import Trophy from "@/components/trophy";

dayjs.locale('ko');
dayjs.extend(relativeTime);

const BattleResultInfo = ({battle}: { battle: IPlayerBattle }) => {
  if (battle.result) {
    return (
      <div className='font-bold'>
        {battleResultTitle(battle.result)}
      </div>
    );
  } else if (battle.rank) {
    return (
      <div className='font-bold'>
        {battle.rank}위
      </div>
    );
  }

  return <div></div>;
}

const BattleTypeIcon = ({type}: { type: BattleType }) => {
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

const BattleInfo = ({battle}: { battle: IPlayerBattle }) => {
  const modeTitle = playerBattleModeTitle(battle);
  const mapName = battle.event ? battle.event.map.name : '알 수 없음';
  return (
    <div className="flex items-center gap-1">
      <div>
        <div className="flex items-center gap-1">
          <BattleTypeIcon type={battle.type}/>
          {battleTypeTitle(battle.type) && (
            <span className="font-bold">{battleTypeTitle(battle.type)}</span>
          )}
        </div>
        <div className="text-sm text-zinc-600">
          {dayjs(battle.battleTime).fromNow()}
        </div>
        <Separator className="my-1"/>
        <BattleResultInfo battle={battle}/>
        <div className="text-sm text-zinc-600">
          {battle.duration && (
            <span>{battle.duration}초</span>
          )}
        </div>
        <Separator className="my-1"/>
        <div className="flex items-center gap-1">
          <BattleModeIcon battle={battle}/>
          <span className="font-bold">{modeTitle}</span>
        </div>
        <div className="text-sm text-zinc-600">
          <span>{mapName}</span>
        </div>
      </div>
      <BattleEventMapImage battleEvent={battle.event}/>
    </div>
  );
}

const BattleModeIcon = ({battle}: { battle: IPlayerBattle }) => {
  const iconSrc = playerBattleIconSrc(battle);
  if (!iconSrc) {
    return null;
  }

  return (
    <Image src={iconSrc} alt="battle event icon" width={24} height={24}/>
  );
}

const battleBackgroundColor = (battle: IPlayerBattle) => {
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

const battleBorderColor = (battle: IPlayerBattle) => {
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

const PowerLevel = ({value}: { value: number }) => {
  return (
    <div className="relative inline-flex items-center justify-center">
      <div
        className="w-6 h-6 rounded-full bg-gradient-to-br from-[#E84BCE] to-[#CF30B5] flex items-center justify-center shadow-lg border-[0.5px] border-black">
        <div className="w-4 h-4 rounded-full bg-[#6c1473] flex items-center justify-center border-[0.5px] border-black">
          <span className="text-white font-bold text-xs" style={{
            textShadow: '-0.5px -0.5px 0 #000, 0.5px -0.5px 0 #000, -0.5px 0.5px 0 #000, 0.5px 0.5px 0 #000'
          }}>
            {value}
          </span>
        </div>
      </div>
    </div>
  );
}


const PlayerTierContainer = ({children}: { children: React.ReactNode }) => (
  <div className="flex items-center gap-1">
    {children}
  </div>
);

const PlayerTier = ({player}: { player: BattlePlayer }) => {
  if (player.soloRankTier) {
    return (
      <PlayerTierContainer>
        <SoloRankTier tier={player.soloRankTier}/>
      </PlayerTierContainer>
    );
  } else if (player.brawler.trophies) {
    return (
      <PlayerTierContainer>
        <Trophy value={player.brawler.trophies}/>
      </PlayerTierContainer>
    );
  }

  return null;
}

const BattleTeamPlayer = (
  {player, brawler, myTag, starPlayerTag}: {
    player: BattlePlayer,
    brawler: Brawler | null,
    myTag: string,
    starPlayerTag: string | null
  }
) => {
  const nameStyle = player.tag === myTag
    ? 'font-bold text-zinc-800' : 'text-zinc-500';

  return (
    <div className="flex flex-col items-center">
      <div className="relative">
        <div className="absolute top-0 w-full flex justify-between z-10 bg-gray-100/50">
          <PowerLevel value={player.brawler.power}/>
          <span className="px-1">
            <PlayerTier player={player}/>
          </span>
        </div>
        <BrawlerProfileImage brawler={brawler}/>
      </div>
      <div className="w-full">
        <div className="flex gap-1">
          <Link
            href={`/players/${encodeURIComponent(player.tag)}`}
            className={cn(nameStyle, 'text-sm', 'flex-1')}
          >
            {player.name}
          </Link>
          {starPlayerTag === player.tag && (
            <span className="text-sm">⭐️</span>
          )}
        </div>
      </div>
    </div>
  );
}

const BattleTeams = (
  {battle, myTag, brawlerList}: { battle: IPlayerBattle, myTag: string, brawlerList: Brawler[] }
) => {
  const brawlers = new BrawlersImpl(brawlerList);
  return (
    <div className="flex flex-col h-full items-center justify-center">
      {battle.teams.map((team, i) => (
        <div key={i} className="flex items-center gap-4">
          {team.map((player, j) => (
            <BattleTeamPlayer
              key={j}
              player={player}
              myTag={myTag}
              starPlayerTag={battle.starPlayerTag}
              brawler={brawlers.find(player.brawler.id)}
            />
          ))}
        </div>
      ))}
    </div>
  );
}

type Props = {
  battle: IPlayerBattle;
  brawlerList: Brawler[];
  myTag: string;
}

export default function PlayerBattle({battle, brawlerList, myTag}: Readonly<Props>) {
  return (
    <div className={`flex p-4 space-x-2 rounded-lg ${battleBackgroundColor(battle)} ${battleBorderColor(battle)}`}>
      <div className="flex gap-2">
        <BattleInfo battle={battle}/>
        <Separator orientation="vertical" className="my-2"/>
      </div>
      <div className="flex-1">
        <BattleTeams battle={battle} myTag={myTag} brawlerList={brawlerList}/>
      </div>
    </div>
  );
}