import {ApiError, fetchGetRenewalStatus, fetchRenewPlayer} from "@/lib/api/api";
import {ApiResponse, createApiResponse} from "@/model/response/ApiResponse";
import {Player} from "@/model/Player";
import {fetchGetPlayer} from "@/lib/api/api";
import {fetchSearchPlayer} from "@/lib/api/api";
import {ListResponse} from "@/model/response/ListResponse";

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

  throw new ApiError(response);
}

export interface PlayerRenewalStatusResponse {
  renewing: boolean;
}

export async function getPlayerRenewalStatus(tag: string): Promise<PlayerRenewalStatusResponse> {
  const response = await fetchGetRenewalStatus(encodeURIComponent(tag));

  if (!response.ok) {
    throw new ApiError(response);
  }

  return await response.json() as PlayerRenewalStatusResponse;
}

export async function renewPlayer(tag: string): Promise<ApiResponse<void>> {
  const response = await fetchRenewPlayer(encodeURIComponent(tag));
  return createApiResponse(response);
}

export async function searchPlayer(query: string): Promise<Player[]> {
  const response = await fetchSearchPlayer(query);

  if (!response.ok) {
    throw new ApiError(response);
  }

  const data = await response.json() as ListResponse<Player>;
  return data.content;
}
