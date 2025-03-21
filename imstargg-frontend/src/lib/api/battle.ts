import {PlayerBattle} from "@/model/PlayerBattle";
import {BattleEventMode} from "@/model/enums/BattleEventMode";
import {BattleResult} from "@/model/enums/BattleResult";
import {BattleType} from "@/model/enums/BattleType";
import {SoloRankTier} from "@/model/enums/SoloRankTier";
import {BattleMode} from "@/model/enums/BattleMode";
import {ApiError, BASE_URL, CacheOptions, SliceResponse} from "@/lib/api/api";

export async function fetchGetBattles(tag: string, page: number = 1, options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/players/${tag}/battles`);
  url.searchParams.append('page', page.toString());
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['players', tag, 'battles', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}


interface PlayerBattleResponse {
  battleTime: Date;
  event: PlayerBattleEventResponse;
  mode: BattleMode;
  type: BattleType;
  result?: BattleResult;
  duration?: number;
  rank?: number;
  trophyChange?: number;
  starPlayerTag: string | null;
  teams: BattlePlayerResponse[][];
}

interface PlayerBattleEventResponse {
  id: number | null;
  mode: BattleEventMode | null;
  mapName: string | null;
  mapImagePath: string | null;
}

interface BattlePlayerResponse {
  tag: string;
  name: string;
  soloRankTier?: SoloRankTier;
  brawler: BattlePlayerBrawlerResponse;
}

interface BattlePlayerBrawlerResponse {
  id: number;
  power: number;
  trophies?: number;
  trophyChange?: number;
}

export async function getBattles(tag: string, page: number = 1): Promise<SliceResponse<PlayerBattle>> {
  const response = await fetchGetBattles(encodeURIComponent(tag), page);

  if (response.ok) {
    const slice = await response.json() as SliceResponse<PlayerBattleResponse>;
    return {
      hasNext: slice.hasNext,
      content: slice.content.map(battle => ({
          ...battle,
          battleTime: new Date(battle.battleTime),
          event: {
            ...battle.event,
            map: {
              name: battle.event.mapName,
              imageUrl: battle.event.mapImagePath ? new URL(battle.event.mapImagePath, process.env.NEXT_PUBLIC_IMAGE_BASE_URL).toString() : null
            }
          },
          teams: battle.teams.map(team =>
            team.map(player => ({
              ...player,
              brawler: {
                ...player.brawler,
              }
            }))
          ),
        }
      ))
    }
  }

  throw await ApiError.create(response);
}
