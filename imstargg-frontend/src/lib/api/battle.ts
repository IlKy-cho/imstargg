import {PlayerBattle} from "@/model/PlayerBattle";
import {BattleEventModeType} from "@/model/enums/BattleEventMode";
import {BattleResultType} from "@/model/enums/BattleResult";
import {BattleTypeType} from "@/model/enums/BattleType";
import {BrawlerRarityType} from "@/model/enums/BrawlerRarity";
import {fetchGetBattles} from "@/lib/api/api";
import {ListResponse} from "@/model/response/ListResponse";
import {SoloRankTierType} from "@/model/enums/SoloRankTier";


interface PlayerBattleResponse {
  battleTime: Date;
  event: BattleEventResponse | null;
  type: BattleTypeType;
  result?: BattleResultType;
  duration?: number;
  rank?: number;
  trophyChange?: number;
  starPlayerTag: string | null;
  teams: BattlePlayerResponse[][];
}

interface BattleEventResponse {
  id: number;
  mode: BattleEventModeType;
  mapName: string;
  mapImageUrl: string | null;
}

interface BattlePlayerResponse {
  tag: string;
  name: string;
  soloRankTier?: SoloRankTierType;
  brawler: BattlePlayerBrawlerResponse;
}

interface BattlePlayerBrawlerResponse {
  id: number | null;
  power: number;
  trophies?: number;
  trophyChange?: number;
}

export async function getBattles(tag: string, page: number = 1): Promise<PlayerBattle[]> {
  const response = await fetchGetBattles(tag, page);

  if (response.ok) {
    const data = await response.json() as ListResponse<PlayerBattleResponse>;
    return data.content.map(battle => ({
        ...battle,
        event: battle.event ? {
          id: battle.event.id,
          mode: battle.event.mode,
          map: {
            name: battle.event.mapName,
            imageUrl: battle.event.mapImageUrl
          }
        } : null,
        teams: battle.teams.map(team =>
          team.map(player => ({
            ...player,
            brawler: {
              ...player.brawler,
            }
          }))
        ),
      }
    ));
  }

  console.log(`Failed to fetch from ${response.url}. status: ${response.status}, body: ${response.body}`);
  throw new Error(`Failed to fetch from ${response.url}.`);
}
