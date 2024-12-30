import {fetchEventSeasoned, fetchEventUnseasoned, fetchGetEvents} from "@/lib/api/api";
import {ListResponse} from "@/model/response/ListResponse";
import BattleEvent from "@/model/BattleEvent";

class ApiError extends Error {
  constructor(public response: Response, message = `Failed to fetch from ${response.url}`) {
    super(message);
  }

  log() {
    console.log(`Failed to fetch from ${this.response.url}. status: ${this.response.status}, body: ${this.response.body} ex: ${this.message}`);
  }
}

export async function getEvents(): Promise<BattleEvent[]> {
  const response = await fetchGetEvents();
  if (response.ok) {
    const data = await response.json() as ListResponse<BattleEvent>;
    return data.content;
  }

  const apiError = new ApiError(response);
  apiError.log();
  throw apiError;
}

export async function eventSeasoned(eventId: number): Promise<void> {
  const response = await fetchEventSeasoned(eventId);

  if (!response.ok) {
    throw new ApiError(response);
  }
}

export async function eventUnseasoned(eventId: number): Promise<void> {
  const response = await fetchEventUnseasoned(eventId);

  if (!response.ok) {
    throw new ApiError(response);
  }
}