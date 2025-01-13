import {PlayerBattle} from "@/model/PlayerBattle";
import {BattleEventMode} from "@/model/enums/BattleEventMode";
import {BattleResult} from "@/model/enums/BattleResult";
import {BattleType} from "@/model/enums/BattleType";
import {fetchGetBattles} from "@/lib/api/api";
import {SoloRankTier} from "@/model/enums/SoloRankTier";
import {SliceResponse} from "@/model/response/SliceResponse";
import {BattleMode} from "@/model/enums/BattleMode";
import {ApiError} from "@/model/response/error";


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
  mapImageUrl: string | null;
}

interface BattlePlayerResponse {
  tag: string;
  name: string;
  soloRankTier?: SoloRankTier;
  brawler: BattlePlayerBrawlerResponse;
}

interface BattlePlayerBrawlerResponse {
  id: number | null;
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
              imageUrl: battle.event.mapImageUrl,
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
