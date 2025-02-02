'use client';

import 'dayjs/locale/ko';
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
import {BattleResult, BattleResultValue} from "@/model/enums/BattleResult";
import {BattleType} from "@/model/enums/BattleType";
import Image from "next/image";
import {Separator} from "@/components/ui/separator";
import {BattlePlayer} from "@/model/BattlePlayer";
import BrawlerProfileImage from "@/components/brawler-profile-image";
import Link from "next/link";
import {cn, cnWithDefault} from "@/lib/utils";
import BattleEventMapImage from "@/components/battle-event-map-image";
import SoloRankTier from "@/components/solo-rank-tier";
import Trophy from "@/components/trophy";
import {BrawlerLink} from "@/components/brawler-link";
import {Tooltip, TooltipContent, TooltipProvider, TooltipTrigger} from "./ui/tooltip";
import {PowerLevel} from "./brawler";
import {createContext, useContext, useEffect, useMemo, useState} from "react";
import {PlayerBattle as PlayerBattleModel, playerBattleMe, playerBattleMyTeam} from "@/model/PlayerBattle";
import {getBattles} from "@/lib/api/battle";
import {Brawler, BrawlerCollection} from "@/model/Brawler";
import {LoadingButton} from "@/components/ui/expansion/loading-button";
import {battleTypeIconSrc, battleTypeTitle} from "@/lib/battle-type";
import {battleResultTitle} from "@/lib/battle-result";
import {playerBattleIconSrc, playerBattleModeTitle} from "@/lib/player-battle";
import {battleEventHref, brawlerHref, playerHref} from "@/config/site";
import {ChartConfig, ChartContainer, ChartTooltip, ChartTooltipContent} from './ui/chart';
import {Card, CardContent, CardHeader, CardTitle} from './ui/card';
import {CartesianGrid, Line, LineChart, XAxis, YAxis} from 'recharts';
import {Player} from "@/model/Player";

dayjs.locale('ko');
dayjs.extend(relativeTime);

interface BattleContextType {
  battles: PlayerBattleModel[];
  page: number;
  loading: boolean;
  hasMore: boolean;
  fetchBattles: () => Promise<void>;
}

const initialState: BattleContextType = {
  battles: [],
  page: 0,
  loading: false,
  hasMore: true,
  fetchBattles: async () => {},
};

const BattleContext = createContext<BattleContextType>(initialState);

type PlayerBattleContentProps = {
  player: Player;
  brawlers: Brawler[];
};

export function PlayerBattleContent({ player, brawlers }: Readonly<PlayerBattleContentProps>) {
  const tag = player.tag;
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
  }

  return (
    <BattleContext.Provider value={{ battles, page, loading, hasMore, fetchBattles }}>
      <div className="flex flex-col sm:flex-row gap-1">
        <div className="flex flex-col gap-1 w-full sm:w-72">
          <PlayerBattleRecentMyTeamStatistics myTag={tag} />
          <PlayerBattleRecentBrawlerStatistics myTag={tag} brawlers={brawlers} />
          <RecentTrophyChange player={player} />
        </div>
        <div className="flex-1">
          <PlayerBattleList tag={tag} brawlerList={brawlers} />
        </div>
      </div>
    </BattleContext.Provider>
  )
}

type PlayerBattleListProps = {
  tag: string;
  brawlerList: Brawler[];
};

export function PlayerBattleList({ tag, brawlerList }: Readonly<PlayerBattleListProps>) {
  const { battles, loading, hasMore, fetchBattles } = useContext(BattleContext);

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
  const shouldUseGrid = battle.teams.length > 5;

  return (
    <div className={`${shouldUseGrid ? 'grid grid-cols-2' : 'flex flex-col'} gap-1 items-center justify-center`}>
      {battle.teams.map((team, i) => (
        <BattleTeam
          key={i}
          team={team}
          myTag={myTag}
          starPlayerTag={battle.starPlayerTag}
          brawlers={brawlers}
        />
      ))}
    </div>
  );
}

function BattleTeam({ team, myTag, starPlayerTag, brawlers }: { team: BattlePlayer[], myTag: string, starPlayerTag: string | null, brawlers: Brawler[] }) {
  const brawlerCollection = new BrawlerCollection(brawlers);
  const shouldSplit = team.length > 3;
  const halfLength = shouldSplit ? Math.ceil(team.length / 2) : team.length;
  
  return (
    <div className="border border-zinc-200 rounded-lg p-1 sm:p-2">
      <div className={`flex ${shouldSplit ? 'flex-col sm:flex-row' : 'flex-row'} items-center gap-2`}>
        <div className="flex items-center gap-2">
          {team.slice(0, halfLength).map((player, j) => (
            <BattleTeamPlayer
              key={j}
              player={player}
              myTag={myTag}
              starPlayerTag={starPlayerTag}
              brawler={brawlerCollection.find(player.brawler.id)}
            />
          ))}
        </div>
        {shouldSplit && team.length > halfLength && (
          <div className="flex items-center gap-2">
            {team.slice(halfLength).map((player, j) => (
              <BattleTeamPlayer
                key={j + halfLength}
                player={player}
                myTag={myTag}
                starPlayerTag={starPlayerTag}
                brawler={brawlerCollection.find(player.brawler.id)}
              />
            ))}
          </div>
        )}
      </div>
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
    <div className="flex flex-col items-center w-16 sm:w-20">
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
        <div className="flex gap-1 sm:text-xs text-[0.625rem]">
          <Link
            href={playerHref(player.tag)}
            className={cn(nameStyle, 'flex-1', 'truncate')}
          >
            {player.name}
          </Link>
          {starPlayerTag === player.tag && (
            <span>⭐️</span>
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

class BattleResultCounter {
  private victoryCount: number = 0;
  private defeatCount: number = 0;
  private drawCount: number = 0;

  constructor() {
    this.victoryCount = 0;
    this.defeatCount = 0;
    this.drawCount = 0;
  }

  public add(battleResult: BattleResult) {
    if (battleResult === BattleResultValue.VICTORY) {
      this.victoryCount++;
    } else if (battleResult === BattleResultValue.DEFEAT) {
      this.defeatCount++;
    } else if (battleResult === BattleResultValue.DRAW) {
      this.drawCount++;
    }
  }

  public getVictoryCount() {
    return this.victoryCount;
  }

  public getDefeatCount() {
    return this.defeatCount;
  }

  public getDrawCount() {
    return this.drawCount;
  }

  public getTotalCount() {
    return this.victoryCount + this.defeatCount + this.drawCount;
  }

  public getWinRate() {
    return (this.victoryCount / (this.victoryCount + this.defeatCount)) * 100;
  }
}


function PlayerBattleRecentMyTeamStatistics({ myTag }: Readonly<{ myTag: string }>) {
  const { battles } = useContext(BattleContext);
  const myTeamPlayerTagToCounter = new Map<string, BattleResultCounter>();
  const tagToName = new Map<string, string>();

  battles.forEach(battle => {
    if (!battle.result) return;

    const myTeam = playerBattleMyTeam(battle);
    myTeam.forEach(player => {
      if (player.tag === myTag) return;

      // 플레이어 이름 저장
      if (!tagToName.has(player.tag)) {
        tagToName.set(player.tag, player.name);
      }

      // 전적 카운터 업데이트
      let counter = myTeamPlayerTagToCounter.get(player.tag);
      if (!counter) {
        counter = new BattleResultCounter();
        myTeamPlayerTagToCounter.set(player.tag, counter);
      }
      counter.add(battle.result!);
    });
  });

  const teamStats = Array.from(myTeamPlayerTagToCounter.entries())
    .map(([tag, counter]) => ({
      tag,
      victories: counter.getVictoryCount(),
      defeats: counter.getDefeatCount(),
      draws: counter.getDrawCount(),
      total: counter.getTotalCount(),
      winRate: counter.getWinRate()
    }))
    .filter(stat => stat.total >= 2)
    .sort((a, b) => b.total - a.total);

  return (
    <div className={cnWithDefault('flex flex-col gap-1')}>
      <h2 className="text-xs sm:text-sm font-bold">
        같은 팀으로 게임한 플레이어 (최근 {battles.length}게임)
      </h2>
      <div className="flex flex-col gap-1">
        {teamStats.map(stat => (
          <div key={stat.tag} className="flex flex-col p-2 border rounded-lg">
            <Link href={playerHref(stat.tag)} className="font-bold text-sm">
              {tagToName.get(stat.tag)!}<span className="text-zinc-500">{stat.tag}</span>
            </Link>
            <ResultStatistics victories={stat.victories} defeats={stat.defeats} draws={stat.draws} total={stat.total} winRate={stat.winRate} />
          </div>
        ))}
      </div>
    </div>
  );
}

function PlayerBattleRecentBrawlerStatistics({ myTag, brawlers }: Readonly<{ myTag: string, brawlers: Brawler[] }>) {
  const { battles } = useContext(BattleContext);
  const brawlerCollection = new BrawlerCollection(brawlers);
  const brawlerIdToCounter = new Map<number, BattleResultCounter>();

  battles.forEach(battle => {
    if (!battle.result) return;
    const me = playerBattleMe(battle, myTag);
    me.forEach(player => {
      const brawler = brawlerCollection.find(player.brawler.id);
      if (!brawler) return;

      let counter = brawlerIdToCounter.get(brawler.id);
      if (!counter) {
        counter = new BattleResultCounter();
        brawlerIdToCounter.set(brawler.id, counter);
      }
      counter.add(battle.result!);
    });
  });

  const brawlerStats = Array.from(brawlerIdToCounter.entries())
    .map(([id, counter]) => ({
      id,
      victories: counter.getVictoryCount(),
      defeats: counter.getDefeatCount(),
      draws: counter.getDrawCount(),
      total: counter.getTotalCount(),
      winRate: counter.getWinRate()
    }))
    .sort((a, b) => b.total - a.total);

  return (
    <div className={cnWithDefault('flex flex-col gap-1')}>
      <h2 className="text-xs sm:text-sm font-bold">
        플레이한 브롤러 (최근 {battles.length}게임)
      </h2>
      <div className="flex flex-col gap-1">
        {brawlerStats.map(stat => (
          <div key={stat.id} className="flex p-2 gap-2 items-center border rounded-lg">
            <Link href={brawlerHref(stat.id)} className="font-bold text-sm">{brawlerCollection.find(stat.id)!.name}</Link>
            <ResultStatistics victories={stat.victories} defeats={stat.defeats} draws={stat.draws} total={stat.total} winRate={stat.winRate} />
          </div>
        ))}
      </div>
    </div>
  );
}

function ResultStatistics({ victories, defeats, draws, total, winRate }: Readonly<{ victories: number, defeats: number, draws: number, total: number, winRate: number }>) {
  return (
    <div className="text-xs flex gap-1">
      <span className="text-blue-600">{victories}승</span>
      <span className="text-red-600">{defeats}패</span>
      {draws > 0 && <span className="text-amber-600">{draws}무</span>}
      <span className="text-zinc-500">{total}게임</span>
      <span className="text-zinc-800">{winRate.toFixed(0)}%</span>
    </div>
  );
}

function RecentTrophyChange({ player }: Readonly<{ player: Player }>) {
  const { battles } = useContext(BattleContext);

  const chartConfig = useMemo(() => ({
    trophy: {
      label: '트로피',
    }
  } satisfies ChartConfig), []);

  const data = useMemo(() => {
    let currentTrophy = player.trophies;
    const battleTrophies: {battleTime: Date, trophy: number}[] = [];
    for (let i = 0; i < battles.length; i++) {
      const battle = battles[i];
      if (battle.trophyChange) {
        battleTrophies.push({
          battleTime: battle.battleTime,
          trophy: currentTrophy
        });
        currentTrophy -= battle.trophyChange;
      } else {
        const battleBrawlersTrophyChange = playerBattleMe(battle, player.tag)
          .map(p => p.brawler.trophyChange)
          .filter((change): change is number => change !== undefined)
          .reduce((acc, change) => acc + change, 0);
        if (battleBrawlersTrophyChange !== 0) {
          battleTrophies.push({
            battleTime: battle.battleTime,
            trophy: currentTrophy
          });
          currentTrophy -= battleBrawlersTrophyChange;
        }
      }
    }
    return battleTrophies.reverse();
  }, [battles]);

  const minTrophy = Math.min(...data.map(d => d.trophy));
  const maxTrophy = Math.max(...data.map(d => d.trophy));

  const padding = Math.round((maxTrophy - minTrophy) * 0.05);

  return (
    <Card>
      <CardHeader className="p-2">
        <CardTitle className="text-xs sm:text-sm">
          트로피 (최근 {battles.length}게임)
        </CardTitle>
      </CardHeader>
      <CardContent className="p-2">
        <ChartContainer config={chartConfig}>
          <LineChart
            accessibilityLayer
            data={data}
          >
            <CartesianGrid vertical={false} />
            <XAxis 
              dataKey="battleTime" 
              tickLine={false}
              axisLine={false}
              tickFormatter={(value) => dayjs(value).format('MM/DD')}
            />
            <YAxis
              domain={[minTrophy - padding, maxTrophy + padding]}
              tickLine={false}
              axisLine={false}
            />
            <ChartTooltip
              content={<ChartTooltipContent hideLabel />}
            />
            <Line
              dataKey="trophy"
              type="linear"
              strokeWidth={2}
              dot={false}
            />
          </LineChart>
        </ChartContainer>
      </CardContent>
    </Card>
  );
}
