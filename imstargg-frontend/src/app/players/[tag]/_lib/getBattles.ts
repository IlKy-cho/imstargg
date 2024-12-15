import PlayerBattle from "@/model/PlayerBattle";
import {BattleEventMode} from "@/model/BattleEventMode";
import {BattleType} from "@/model/BattleType";
import {BattleResult} from "@/model/BattleResult";
import {BrawlerRarity} from "@/model/BrawlerRarity";

interface PlayerBattleListResponse {
  content: PlayerBattleResponse[];
}

interface PlayerBattleResponse {
  battleTime: string;
  event: BattleEventResponse | null;
  type: BattleType;
  result: BattleResult | null;
  duration: number | null;
  rank: number | null;
  trophyChange: number | null;
  starPlayerTag: string | null;
  teams: BattlePlayerResponse[][];
}

interface BattleEventResponse {
  id: number;
  mode: BattleEventMode;
  mapCode: string;
  mapName: string;
}

interface BattlePlayerResponse {
  tag: string;
  name: string;
  brawler: BattlePlayerBrawlerResponse;
}

interface BattlePlayerBrawlerResponse {
  id: number | null;
  name: string | null;
  rarity: BrawlerRarity | null;
  power: number;
  trophies: number | null;
  trophyChange: number | null;
}

export async function getBattles(tag: string, page: number = 1): Promise<PlayerBattle[]> {
  const url = new URL(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/players/${tag}/battles`);
  url.searchParams.append('page', page.toString());
  console.log(`Fetch from ${url}`);
  const response = await fetch(url, {
    next: {
      tags: ['players', tag, 'battles']
    }
  });
  
  if (!response.ok) {
    if (response.status === 404) {
      throw new Error('Player not found');
    }
    throw new Error('Failed to fetch battle data');
  }
  
  const data = await response.json() as PlayerBattleListResponse;
  return data.content.map(battle => ({
    battleTime: new Date(battle.battleTime),
    event: battle.event ? {
      id: battle.event.id,
      mode: battle.event.mode,
      map: {
        code: battle.event.mapCode,
        name: battle.event.mapName
      }
    } : null,
    type: battle.type,
    result: battle.result,
    duration: battle.duration,
    rank: battle.rank,
    trophyChange: battle.trophyChange,
    starPlayerTag: battle.starPlayerTag,
    teams: battle.teams.map(team => 
      team.map(player => ({
        tag: player.tag,
        name: player.name,
        brawler: {
          id: player.brawler.id,
          name: player.brawler.name,
          rarity: player.brawler.rarity,
          power: player.brawler.power,
          trophies: player.brawler.trophies,
          trophyChange: player.brawler.trophyChange
        }
      }))
    )
  }));
}
