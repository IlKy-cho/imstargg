import {fetchGetBattleEvent, fetchGetBattleEvents} from "@/lib/api/api";
import {BattleEvent} from "@/model/BattleEvent";
import {BattleEventMode} from "@/model/enums/BattleEventMode";
import {ListResponse} from "@/model/response/ListResponse";
import {ApiError} from "@/model/response/error";

interface BattleEventResponse {
  id: number;
  mode: BattleEventMode;
  mapName: string | null;
  mapImageUrl: string | null;
}

export async function getBattleEvents(): Promise<BattleEvent[]> {
  const aWeekAgo = new Date();
  aWeekAgo.setDate(aWeekAgo.getDate() - 7);

  const response = await fetchGetBattleEvents(
    aWeekAgo, { revalidate: 60 * 60 }
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

  throw await ApiError.create(response);
}

export async function getBattleEvent(id: number): Promise<BattleEvent | null> {
  const response = await fetchGetBattleEvent(id, { revalidate: 60 * 60 });

  if (response.ok) {
    const data = await response.json() as BattleEventResponse;
    return {
      ...data,
      map: {
        name: data.mapName,
        imageUrl: data.mapImageUrl,
      }
    };
  } else if (response.status === 404) {
    return null;
  }

  throw await ApiError.create(response);
}
