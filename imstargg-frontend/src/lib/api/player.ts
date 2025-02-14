import {
  ApiError,
  BASE_URL,
  CacheOptions, ListResponse,
} from "@/lib/api/api";
import {Player} from "@/model/Player";
import { PlayerBrawler } from "@/model/PlayerBrawler";

export async function fetchGetPlayer(tag: string, options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/players/${tag}`);
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['players', tag],
      revalidate: options.revalidate
    }
  });
}

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

export async function fetchGetRenewalStatus(tag: string): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/players/${tag}/renewal-status`);
  return await fetch(url);
}

export async function getPlayerRenewalStatus(tag: string): Promise<PlayerRenewalStatusResponse> {
  const response = await fetchGetRenewalStatus(encodeURIComponent(tag));

  if (!response.ok) {
    throw await ApiError.create(response);
  }

  return await response.json() as PlayerRenewalStatusResponse;
}

export async function fetchGetRenewalStatusNew(tag: string): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/players/${tag}/renewal-status-new`);
  return await fetch(url);
}

export async function getPlayerRenewalStatusNew(tag: string): Promise<PlayerRenewalStatusResponse> {
  const response = await fetchGetRenewalStatusNew(encodeURIComponent(tag));

  if (!response.ok) {
    throw await ApiError.create(response);
  }

  return await response.json() as PlayerRenewalStatusResponse;
}

export async function fetchRenewPlayer(tag: string): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/players/${tag}/renew`);

  return await fetch(url, {
    method: 'POST',
  });
}

export async function renewPlayer(tag: string): Promise<void> {
  const response = await fetchRenewPlayer(encodeURIComponent(tag));
  if (!response.ok) {
    throw await ApiError.create(response);
  }
}

export async function fetchRenewNewPlayer(tag: string): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/players/${tag}/renew-new`);

  return await fetch(url, {
    method: 'POST',
  });
}

export async function renewNewPlayer(tag: string): Promise<void> {
  const response = await fetchRenewNewPlayer(encodeURIComponent(tag));
  if (!response.ok) {
    throw await ApiError.create(response);
  }
}

export async function fetchSearchPlayer(query: string, options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/player/search`);
  url.searchParams.append('query', query);
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['player', 'search', query],
      revalidate: options.revalidate,
    }
  });
}

export async function searchPlayer(query: string): Promise<Player[]> {
  const response = await fetchSearchPlayer(query);

  if (!response.ok) {
    throw await ApiError.create(response);
  }

  const data = await response.json() as ListResponse<Player>;
  return data.content;
}

export async function fetchGetPlayerBrawlers(tag: string, options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/players/${tag}/brawlers`);
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['player', tag, 'brawlers'],
      revalidate: options.revalidate,
    }
  });
}

export async function getPlayerBrawlers(tag: string): Promise<PlayerBrawler[]> {
  const response = await fetchGetPlayerBrawlers(encodeURIComponent(tag));
  if (!response.ok) {
    throw await ApiError.create(response);
  }

  const data = await response.json() as ListResponse<PlayerBrawler>;
  return data.content;
}