import {TrophyRange} from "@/model/enums/TrophyRange";
import {SoloRankTierRange} from "@/model/enums/SoloRankTierRange";
import {BrawlerResultStatistics} from "@/model/statistics/BrawlerResultStatistics";
import {BrawlerRankStatistics} from "@/model/statistics/BrawlerRankStatistics";
import {BrawlersRankStatistics} from "@/model/statistics/BrawlersRankStatistics";
import {BrawlersResultStatistics} from "@/model/statistics/BrawlersResultStatistics";
import {BrawlerEnemyResultStatistics} from "@/model/statistics/BrawlerEnemyResultStatistics";
import {BattleEventResultStatistics} from "@/model/statistics/BattleEventResultStatistics";
import {BattleEventMode} from "@/model/enums/BattleEventMode";
import {ApiError, BASE_URL, CacheOptions, ListResponse} from "@/lib/api/api";
import {BattleMode} from "@/model/enums/BattleMode";
import {BrawlerItemOwnership} from "@/model/statistics/BrawlerItemOwnership";

export async function fetchGetBattleEventBrawlerResultStatistics(
  eventId: number,
  date: Date,
  duplicateBrawler: boolean,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/events/${eventId}/result/brawler`);
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  url.searchParams.append('duplicateBrawler', duplicateBrawler.toString());
  if (trophyRange) {
    url.searchParams.append('trophyRange', trophyRange);
  }
  if (soloRankTierRange) {
    url.searchParams.append('soloRankTierRange', soloRankTierRange);
  }
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['statistics', 'events', eventId.toString(), 'result', 'brawler', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

interface BattleEventResultStatisticsResponse {
  event: {
    id: number;
    mode: BattleEventMode;
    battleMode: BattleMode;
    mapName: string | null;
    mapImagePath: string | null;
  };
  totalBattleCount: number;
  winRate: number;
  starPlayerRate: number;
}

export async function getBattleEventBrawlerResultStatistics(
  eventId: number,
  date: Date,
  duplicateBrawler: boolean,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null
): Promise<BrawlerResultStatistics[]> {
  const response = await fetchGetBattleEventBrawlerResultStatistics(
    eventId, date, duplicateBrawler, trophyRange, soloRankTierRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlerResultStatistics>;
    return data.content.sort((a, b) => b.winRate - a.winRate);
  }

  throw await ApiError.create(response);
}


export async function fetchGetBattleEventBrawlersResultStatistics(
  eventId: number,
  date: Date,
  duplicateBrawler: boolean,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/events/${eventId}/result/brawlers`);
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  url.searchParams.append('duplicateBrawler', duplicateBrawler.toString());
  if (trophyRange) {
    url.searchParams.append('trophyRange', trophyRange);
  }
  if (soloRankTierRange) {
    url.searchParams.append('soloRankTierRange', soloRankTierRange);
  }
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['statistics', 'events', eventId.toString(), 'result', 'brawlers', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function getBattleEventBrawlersResultStatistics(
  eventId: number,
  date: Date,
  duplicateBrawler: boolean,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null
): Promise<BrawlersResultStatistics[]> {
  const response = await fetchGetBattleEventBrawlersResultStatistics(
    eventId, date, duplicateBrawler, trophyRange, soloRankTierRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlersResultStatistics>;
    return data.content.sort((a, b) => b.winRate - a.winRate);
  }

  throw await ApiError.create(response);
}

export async function fetchGetBattleEventResultBrawlerEnemyStatistics(
  eventId: number,
  date: Date,
  duplicateBrawler: boolean,
  trophyRange?: TrophyRange,
  soloRankTierRange?: SoloRankTierRange,
  options?: CacheOptions
) {
  const url = new URL(`${BASE_URL}/api/v1/statistics/events/{eventId}/result/brawler-enemy`);
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  url.searchParams.append('duplicateBrawler', duplicateBrawler.toString());
  if (trophyRange) {
    url.searchParams.append('trophyRange', trophyRange);
  }
  if (soloRankTierRange) {
    url.searchParams.append('soloRankTierRange', soloRankTierRange);
  }
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['statistics', 'events', eventId.toString(), 'result', 'brawler-enemy', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function getBattleEventResultBrawlerEnemyStatistics(
  eventId: number,
  date: Date,
  duplicateBrawler: boolean,
  trophyRange?: TrophyRange,
  soloRankTierRange?: SoloRankTierRange
) {
  const response = await fetchGetBattleEventResultBrawlerEnemyStatistics(
    eventId, date, duplicateBrawler, trophyRange, soloRankTierRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlerEnemyResultStatistics>;
    return data.content.sort((a, b) => b.winRate - a.winRate);
  }

  throw await ApiError.create(response);
}


export async function fetchGetBattleEventBrawlerRankStatistics(
  eventId: number,
  date: Date,
  trophyRange: TrophyRange,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/events/${eventId}/rank/brawler`);
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  url.searchParams.append('trophyRange', trophyRange);
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['statistics', 'events', eventId.toString(), 'rank', 'brawler', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function getBattleEventBrawlerRankStatistics(
  eventId: number,
  date: Date,
  trophyRange: TrophyRange
): Promise<BrawlerRankStatistics[]> {
  const response = await fetchGetBattleEventBrawlerRankStatistics(
    eventId, date, trophyRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlerRankStatistics>;
    return data.content.sort((a, b) => a.averageRank - b.averageRank);
  }

  throw await ApiError.create(response);
}


export async function fetchGetBattleEventBrawlersRankStatistics(
  eventId: number,
  date: Date,
  trophyRange: TrophyRange,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/events/${eventId}/rank/brawlers`);
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  url.searchParams.append('trophyRange', trophyRange);
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['statistics', 'events', eventId.toString(), 'rank', 'brawlers', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function getBattleEventBrawlersRankStatistics(
  eventId: number,
  date: Date,
  trophyRange: TrophyRange
): Promise<BrawlersRankStatistics[]> {
  const response = await fetchGetBattleEventBrawlersRankStatistics(
    eventId, date, trophyRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlersRankStatistics>;
    return data.content.sort((a, b) => a.averageRank - b.averageRank);
  }

  throw await ApiError.create(response);
}


export async function fetchGetBrawlerResultStatistics(
  date: Date,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/brawler-result`);
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  if (trophyRange) {
    url.searchParams.append('trophyRange', trophyRange);
  }
  if (soloRankTierRange) {
    url.searchParams.append('soloRankTierRange', soloRankTierRange);
  }
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['statistics', 'brawler-result', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function getBrawlerResultStatistics(
  date: Date,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null
): Promise<BrawlerResultStatistics[]> {
  const response = await fetchGetBrawlerResultStatistics(
    date, trophyRange, soloRankTierRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlerResultStatistics>;
    return data.content.sort((a, b) => b.winRate - a.winRate);
  }

  throw await ApiError.create(response);
}

export async function fetchGetBrawlerBattleEventResultStatistics(
  brawlerId: number,
  date: Date,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/brawlers/${brawlerId}/result`);
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  if (trophyRange) {
    url.searchParams.append('trophyRange', trophyRange);
  }
  if (soloRankTierRange) {
    url.searchParams.append('soloRankTierRange', soloRankTierRange);
  }
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['statistics', 'brawlers', brawlerId.toString(), 'result', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function getBrawlerBattleEventResultStatistics(
  brawlerId: number,
  date: Date,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null
): Promise<BattleEventResultStatistics[]> {
  const response = await fetchGetBrawlerBattleEventResultStatistics(
    brawlerId, date, trophyRange, soloRankTierRange,
    // {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BattleEventResultStatisticsResponse>;
    return data.content
      .sort((a, b) => b.winRate - a.winRate)
      .map(stat => ({
        event: {
          id: stat.event.id,
          mode: stat.event.mode,
          battleMode: stat.event.battleMode,
          map: {
            name: stat.event.mapName,
            imageUrl: stat.event.mapImagePath ? new URL(stat.event.mapImagePath, process.env.NEXT_PUBLIC_IMAGE_BASE_URL).toString() : null
          }
        },
        totalBattleCount: stat.totalBattleCount,
        winRate: stat.winRate,
        starPlayerRate: stat.starPlayerRate
      }));
  }

  throw await ApiError.create(response);
}


export async function fetchGetBrawlerBrawlersResultStatistics(
  brawlerId: number,
  date: Date,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/brawlers/${brawlerId}/brawlers-result`);
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  if (trophyRange) {
    url.searchParams.append('trophyRange', trophyRange);
  }
  if (soloRankTierRange) {
    url.searchParams.append('soloRankTierRange', soloRankTierRange);
  }
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['statistics', 'brawlers', brawlerId.toString(), 'brawlers-result', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function getBrawlerBrawlersResultStatistics(
  brawlerId: number,
  date: Date,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null
): Promise<BrawlersResultStatistics[]> {
  const response = await fetchGetBrawlerBrawlersResultStatistics(
    brawlerId, date, trophyRange, soloRankTierRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlersResultStatistics>;
    return data.content.map(stat => {
      const sortedBrawlerIds = [brawlerId, ...stat.brawlerIds.filter(id => id !== brawlerId)];
      return {
        ...stat,
        brawlerIds: sortedBrawlerIds
      };
    }).sort((a, b) => b.winRate - a.winRate);
  }

  throw await ApiError.create(response);
}


export async function fetchGetBrawlerEnemyResultStatistics(
  brawlerId: number,
  date: Date,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/brawlers/${brawlerId}/enemy-result`);
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  if (trophyRange) {
    url.searchParams.append('trophyRange', trophyRange);
  }
  if (soloRankTierRange) {
    url.searchParams.append('soloRankTierRange', soloRankTierRange);
  }
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['statistics', 'brawlers', brawlerId.toString(), 'enemy-result', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function getBrawlerEnemyResultStatistics(
  brawlerId: number,
  date: Date,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null
): Promise<BrawlerEnemyResultStatistics[]> {
  const response = await fetchGetBrawlerEnemyResultStatistics(
    brawlerId, date, trophyRange, soloRankTierRange,
    {revalidate: 60 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlerEnemyResultStatistics>;
    return data.content.sort((a, b) => b.winRate - a.winRate);
  }

  throw await ApiError.create(response);
}

export async function fetchGetBrawlerOwnershipRate(brawlerId: number, options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/brawlers/${brawlerId}/ownership`);
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['statistics', 'brawlers', brawlerId.toString(), 'ownership'],
      revalidate: options.revalidate
    }
  });
}

export async function getBrawlerOwnershipRate(brawlerId: number): Promise<BrawlerItemOwnership> {
  const response = await fetchGetBrawlerOwnershipRate(brawlerId, {revalidate: 60 * 60});

  if (!response.ok) {
    throw await ApiError.create(response);
  }

  return await response.json() as BrawlerItemOwnership;
}
