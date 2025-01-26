import {ApiError, BASE_URL, CacheOptions, ListResponse} from "@/lib/api/api";
import {BattleEvent} from "@/model/BattleEvent";
import {BattleEventMode} from "@/model/enums/BattleEventMode";

export async function fetchGetBattleEvents(date: Date, options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/brawlstars/events`);
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['brawlstars', 'events', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

interface BattleEventResponse {
  id: number;
  mode: BattleEventMode;
  mapName: string | null;
  mapImagePath: string | null;
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
        imageUrl: event.mapImagePath ? new URL(event.mapImagePath, process.env.NEXT_PUBLIC_IMAGE_BASE_URL).toString() : null
      }
    }));
  }

  throw await ApiError.create(response);
}


export async function fetchGetBattleEvent(id: number, options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/brawlstars/events/${id}`);
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['brawlstars', 'events', id.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function getBattleEvent(id: number): Promise<BattleEvent | null> {
  const response = await fetchGetBattleEvent(id, { revalidate: 60 * 60 });

  if (response.ok) {
    const data = await response.json() as BattleEventResponse;
    return {
      ...data,
      map: {
        name: data.mapName,
        imageUrl: data.mapImagePath ? new URL(data.mapImagePath, process.env.NEXT_PUBLIC_IMAGE_BASE_URL).toString() : null
      }
    };
  } else if (response.status === 404) {
    return null;
  }

  throw await ApiError.create(response);
}
