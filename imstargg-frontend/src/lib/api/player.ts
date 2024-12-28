import {fetchGetRenewalStatus, fetchRenewPlayer} from "@/lib/api/api";
import {ApiResponse, createApiResponse} from "@/model/response/ApiResponse";
import {Player} from "@/model/Player";
import {fetchGetPlayer} from "@/lib/api/api";
import {fetchSearchPlayer} from "@/lib/api/api";
import {ListResponse} from "@/model/response/ListResponse";

export interface PlayerResponse {
  player: Player | null;
}

export async function getPlayer(tag: string): Promise<PlayerResponse> {
  const response = await fetchGetPlayer(encodeURIComponent(tag));

  if (response.ok) {
    const player = await response.json() as Player;
    return {
      player: {
        ...player,
        updatedAt: new Date(player.updatedAt),
      }
    };
  } else if (response.status === 404) {
    return {player: null};
  }

  console.log(`Failed to fetch from ${response.url}. status: ${response.status}, body: ${response.body}`);
  throw new Error(`Failed to fetch from ${response.url}.`);
}

export interface PlayerRenewalStatusResponse {
  renewing: boolean;
}

export async function getPlayerRenewalStatus(tag: string): Promise<PlayerRenewalStatusResponse> {
  const response = await fetchGetRenewalStatus(encodeURIComponent(tag));

  if (!response.ok) {
    console.log(`Error status: ${response.status}`);
    console.log(`Error body: ${await response.json()}`);
    throw new Error(`Failed to fetch from ${response.url}.`);
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
    throw new Error('Failed to fetch player data');
  }

  const data = await response.json() as ListResponse<Player>;
  return data.content;
}
