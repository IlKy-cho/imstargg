import {ApiError, fetchGetEvents, fetchUploadMapImage, ListResponse,} from "@/lib/api/api";
import {BattleEvent} from "@/model/BattleEvent";

export async function getEvents(): Promise<BattleEvent[]> {
  const response = await fetchGetEvents();
  if (response.ok) {
    const data = await response.json() as ListResponse<BattleEvent>;
    return data.content
      .map(event => ({
        ...event,
        latestBattleTime: event.latestBattleTime ? new Date(event.latestBattleTime) : null,
      }));
  }

  throw await ApiError.create(response);
}

export async function uploadMapImage(eventId: number, image: File) {
  const response = await fetchUploadMapImage(eventId, image);
  if (!response.ok) {
    throw await ApiError.create(response);
  }
}