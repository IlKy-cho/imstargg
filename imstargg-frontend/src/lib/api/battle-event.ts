import {ApiError, BASE_URL, CacheOptions, ListResponse} from "@/lib/api/api";
import {BattleEvent} from "@/model/BattleEvent";
import {BattleEventMode} from "@/model/enums/BattleEventMode";
import {BattleMode} from "@/model/enums/BattleMode";
import {RotationBattleEvent} from "@/model/RotationBattleEvent";

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
  battleMode: BattleMode | null;
}

export async function getBattleEvents(): Promise<BattleEvent[]> {
  const aWeekAgo = new Date();
  aWeekAgo.setDate(aWeekAgo.getDate() - 7);

  const response = await fetchGetBattleEvents(
    aWeekAgo, { revalidate: 5 * 60 }
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
  const response = await fetchGetBattleEvent(id, { revalidate: 5 * 60 });

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

export async function fetchGetRotationBattleEvents(options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/brawlstars/event/rotation`);
  if (!options) {
    return await fetch(url);
  }
  return await fetch(url, {
    next: {
      tags: ['brawlstars', 'event', 'rotation'],
      revalidate: options.revalidate
    }
  });
}

interface RotationBattleEventResponse {
  event: BattleEventResponse;
  startTime: Date;
  endTime: Date;
}

export async function getRotationBattleEvents(): Promise<RotationBattleEvent[]> {
  const response = await fetchGetRotationBattleEvents({ revalidate: 60 });

  if (!response.ok) {
    throw await ApiError.create(response);
  }

  const data = await response.json() as ListResponse<RotationBattleEventResponse>;
  return data.content.map(event => ({
    event: {
      ...event.event,
      map: {
        name: event.event.mapName,
        imageUrl: event.event.mapImagePath ? new URL(event.event.mapImagePath, process.env.NEXT_PUBLIC_IMAGE_BASE_URL).toString() : null
      }
    },
    startTime: event.startTime,
    endTime: event.endTime
  }));
}
