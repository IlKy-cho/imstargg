'use client';

import {PlayerBattle as IPlayerBattle} from "@/model/PlayerBattle";
import 'dayjs/locale/ko';
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
import {Brawler, BrawlersImpl} from "@/model/Brawler";
import {battleResultTitle, battleTypeTitle, playerBattleModeTitle} from "./title";
import {BattleResult} from "@/model/enums/BattleResult";
import {BattleTypeType} from "@/model/enums/BattleType";
import {battleTypeIconSrc, BrawlStarsIconSrc, playerBattleIconSrc, soloRankTierIconSrc} from "@/components/icon";
import Image from "next/image";
import {Separator} from "@/components/ui/separator";
import {BattlePlayer} from "@/model/BattlePlayer";
import BrawlerProfileImage from "@/components/brawler-profile-image";
import {soloRankTierNumber, SoloRankTierType} from "@/model/enums/SoloRankTier";
import {soloRankTierColor} from "@/components/color";
import Link from "next/link";
import {cn} from "@/lib/utils";

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

const BattleTypeIcon = ({type}: { type: BattleTypeType }) => {
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
      <div>
        <BattleEventMapImage battle={battle}/>
      </div>
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

const BattleEventMapImage = ({battle}: { battle: IPlayerBattle }) => {
  const mapIconSrc = battle.event ? battle.event.map.imageUrl : null;
  if (!mapIconSrc) {
    return (
      <div className="w-20 h-32 bg-gray-200 flex items-center justify-center text-gray-500 text-sm">
        <div>
          맵 이미지
        </div>
        <div>
          없음
        </div>
      </div>
    );
  }

  return (
    <Image
      src={mapIconSrc}
      alt="battle event map"
      width={100}
      height={100}
    />
  );
}

const BattleEventInfo = ({battle}: { battle: IPlayerBattle }) => {
  const modeTitle = playerBattleModeTitle(battle);
  const mapName = battle.event ? battle.event.map.name : '알 수 없음';
  return (
    <div className="flex flex-col items-center">
      <div className="text-center">
        <div className="flex items-center justify-center gap-1">
          <BattleModeIcon battle={battle}/>
          <span>{modeTitle}</span>
        </div>
        <div className="text-sm text-zinc-600">
          <span>{mapName}</span>
        </div>
      </div>
      <BattleEventMapImage battle={battle}/>
    </div>
  );
}

const battleBackgroundColor = (battle: IPlayerBattle) => {
  if (battle.result) {
    switch (battle.result) {
      case BattleResult.VICTORY:
        return 'bg-blue-100/50';
      case BattleResult.DEFEAT:
        return 'bg-red-100/50';
      case BattleResult.DRAW:
        return 'bg-amber-100/50';
    }
  }

  return 'bg-zinc-100/50';
}

const battleBorderColor = (battle: IPlayerBattle) => {
  if (battle.result) {
    switch (battle.result) {
      case BattleResult.VICTORY:
        return 'border-l-4 border-blue-500';
      case BattleResult.DEFEAT:
        return 'border-l-4 border-red-500';
      case BattleResult.DRAW:
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

const TrophyIcon = () => {
  return (<Image
    src={BrawlStarsIconSrc.TROPHY}
    alt="trophy icon"
    width={20}
    height={20}
  />);
};

const SoloRankTierIcon = ({tier}: { tier: SoloRankTierType }) => {
  return (<Image
    src={soloRankTierIconSrc(tier)}
    alt="rank tier icon"
    width={24}
    height={24}
  />);
};

const PlayerTierContainer = ({children}: { children: React.ReactNode }) => (
  <div className="flex items-center gap-1">
    {children}
  </div>
);

const PlayerTier = ({player}: { player: BattlePlayer }) => {
  if (player.soloRankTier) {
    return (
      <PlayerTierContainer>
        <SoloRankTierIcon tier={player.soloRankTier}/>
        {player.soloRankTier && (
          <span className={'text-[' + soloRankTierColor(player.soloRankTier) + ']'}>
            {soloRankTierNumber(player.soloRankTier)}
          </span>
        )}
      </PlayerTierContainer>
    );
  } else if (player.brawler.trophies) {
    return (
      <PlayerTierContainer>
        <TrophyIcon/>
        <span className="text-amber-500">
          {player.brawler.trophies.toLocaleString()}
        </span>
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
  const nameStyle = player.tag === myTag ? 'font-bold text-black' : 'text-zinc-700';

  return (
    <div className="flex flex-col items-center">
      <div className="flex w-full justify-between">
        <PowerLevel value={player.brawler.power}/>
        <PlayerTier player={player}/>
      </div>
      <BrawlerProfileImage brawler={brawler} size={20}/>
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