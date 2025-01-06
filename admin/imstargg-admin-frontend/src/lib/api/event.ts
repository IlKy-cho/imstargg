import {ApiError, fetchGetEvents, fetchUploadMapImage,} from "@/lib/api/api";
import {ListResponse} from "@/model/response/ListResponse";
import BattleEvent from "@/model/BattleEvent";

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

  const apiError = new ApiError(response);
  apiError.log();
  throw apiError;
}

export async function uploadMapImage(eventId: number, image: File) {
  const response = await fetchUploadMapImage(eventId, image);
  if (!response.ok) {
    const apiError = new ApiError(response);
    apiError.log();
    throw apiError;
  }
}