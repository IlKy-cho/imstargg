import {API_BASE_URL, ApiError, ListResponse,} from "@/lib/api/api";
import {BattleEvent} from "@/model/BattleEvent";

export async function fetchGetEvents(): Promise<Response> {
  const url = new URL(`${API_BASE_URL}/admin/api/events`);
  return await fetch(url);
}

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

export async function fetchUploadMapImage(eventId: number, image: File): Promise<Response> {
  const url = new URL(`${API_BASE_URL}/admin/api/events/${eventId}/image`);

  const formData = new FormData();
  formData.append('image', image);

  return await fetch(url, {
    method: 'PUT',
    body: formData
  });
}

export async function uploadMapImage(eventId: number, image: File) {
  const response = await fetchUploadMapImage(eventId, image);
  if (!response.ok) {
    throw await ApiError.create(response);
  }
}