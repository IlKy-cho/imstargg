import {ApiError, fetchGetBattleEvents} from "@/lib/api/api";
import {BattleEvent} from "@/model/BattleEvent";
import {BattleEventMode} from "@/model/enums/BattleEventMode";
import {ListResponse} from "@/model/response/ListResponse";

interface BattleEventResponse {
  id: number;
  mode: BattleEventMode;
  mapName: string | null;
  mapImageUrl: string | null;
}

export async function getBattleEvents(): Promise<BattleEvent[]> {
  const twoWeeksAgo = new Date();
  twoWeeksAgo.setDate(twoWeeksAgo.getDate() - 14);

  const response = await fetchGetBattleEvents(
    twoWeeksAgo, { revalidate: 60 * 60 }
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BattleEventResponse>;
    return data.content.map(event => ({
      ...event,
      map: {
        name: event.mapName,
        imageUrl: event.mapImageUrl,
      }
    }));
  }

  throw new ApiError(response);
}