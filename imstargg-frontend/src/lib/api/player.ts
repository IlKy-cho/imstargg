import {fetchGetRenewalStatus, fetchRenewPlayer} from "@/lib/api/api";
import {Player} from "@/model/Player";
import {fetchGetPlayer} from "@/lib/api/api";
import {fetchSearchPlayer} from "@/lib/api/api";
import {ListResponse} from "@/model/response/ListResponse";
import {ApiError} from "@/model/response/error";

export async function getPlayer(tag: string): Promise<Player | null> {
  const response = await fetchGetPlayer(encodeURIComponent(tag));

  if (response.ok) {
    const player = await response.json() as Player;
    return {
      ...player,
      updatedAt: new Date(player.updatedAt),
    };
  } else if (response.status === 404) {
    return null;
  }

  throw await ApiError.create(response);
}

export interface PlayerRenewalStatusResponse {
  renewing: boolean;
}

export async function getPlayerRenewalStatus(tag: string): Promise<PlayerRenewalStatusResponse> {
  const response = await fetchGetRenewalStatus(encodeURIComponent(tag));

  if (!response.ok) {
    throw await ApiError.create(response);
  }

  return await response.json() as PlayerRenewalStatusResponse;
}

export async function renewPlayer(tag: string): Promise<void> {
  const response = await fetchRenewPlayer(encodeURIComponent(tag));
  if (!response.ok) {
    throw await ApiError.create(response);
  }
}

export async function searchPlayer(query: string): Promise<Player[]> {
  const response = await fetchSearchPlayer(query);

  if (!response.ok) {
    throw await ApiError.create(response);
  }

  const data = await response.json() as ListResponse<Player>;
  return data.content;
}
