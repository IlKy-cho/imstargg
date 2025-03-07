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
import {BrawlerItemOwnership} from "@/model/statistics/BrawlerItemOwnership";
import {DateRange} from "@/model/enums/DateRange";

export async function fetchGetBattleEventBrawlerResultStatistics(
  eventId: number,
  date: Date,
  dateRange: DateRange,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/events/${eventId}/result/brawler`);
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  url.searchParams.append('dateRange', dateRange);
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
  dateRange: DateRange,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null
): Promise<BrawlerResultStatistics[]> {
  const response = await fetchGetBattleEventBrawlerResultStatistics(
    eventId, date, dateRange, trophyRange, soloRankTierRange,
    {revalidate: 5 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlerResultStatistics>;
    return data.content.sort((a, b) => b.winRate - a.winRate);
  }

  throw await ApiError.create(response);
}


export async function fetchGetBattleEventBrawlersResultStatistics(
  eventId: number,
  brawlerId: number,
  date: Date,
  dateRange: DateRange,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/events/${eventId}/result/brawlers`);
  url.searchParams.append('brawlerId', brawlerId.toString());
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  url.searchParams.append('dateRange', dateRange);
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
  brawlerId: number,
  date: Date,
  dateRange: DateRange,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null
): Promise<BrawlersResultStatistics[]> {
  const response = await fetchGetBattleEventBrawlersResultStatistics(
    eventId, brawlerId, date, dateRange, trophyRange, soloRankTierRange,
    {revalidate: 5 * 60}
  );

  if (!response.ok) {
    throw await ApiError.create(response);
  }

  const data = await response.json() as ListResponse<BrawlersResultStatistics>;
  return data.content
    .map(stat => {
      const sortedBrawlerIds = [brawlerId, ...stat.brawlerIds.filter(id => id != brawlerId)];
      return {
        ...stat,
        brawlerIds: sortedBrawlerIds
      };
    })
    .sort((a, b) => b.winRate - a.winRate);
}

export async function fetchGetBattleEventResultBrawlerEnemyStatistics(
  eventId: number,
  brawlerId: number,
  date: Date,
  dateRange: DateRange,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null,
  options?: CacheOptions
) {
  const url = new URL(`${BASE_URL}/api/v1/statistics/events/${eventId}/result/brawler-enemy`);
  url.searchParams.append('brawlerId', brawlerId.toString());
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  url.searchParams.append('dateRange', dateRange);
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
  brawlerId: number,
  date: Date,
  dateRange: DateRange,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null
) {
  const response = await fetchGetBattleEventResultBrawlerEnemyStatistics(
    eventId, brawlerId, date, dateRange, trophyRange, soloRankTierRange,
    {revalidate: 5 * 60}
  );

  if (!response.ok) {
    throw await ApiError.create(response);
  }

  const data = await response.json() as ListResponse<BrawlerEnemyResultStatistics>;
  return data.content.sort((a, b) => a.winRate - b.winRate);
}


export async function fetchGetBattleEventBrawlerRankStatistics(
  eventId: number,
  date: Date,
  dateRange: DateRange,
  trophyRange: TrophyRange,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/events/${eventId}/rank/brawler`);
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  url.searchParams.append('dateRange', dateRange);
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
  dateRange: DateRange,
  trophyRange: TrophyRange
): Promise<BrawlerRankStatistics[]> {
  const response = await fetchGetBattleEventBrawlerRankStatistics(
    eventId, date, dateRange, trophyRange,
    {revalidate: 5 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlerRankStatistics>;
    return data.content.sort((a, b) => a.averageRank - b.averageRank);
  }

  throw await ApiError.create(response);
}


export async function fetchGetBattleEventBrawlersRankStatistics(
  eventId: number,
  brawlerId: number,
  date: Date,
  dateRange: DateRange,
  trophyRange: TrophyRange,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/events/${eventId}/rank/brawlers`);
  url.searchParams.append('brawlerId', brawlerId.toString());
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  url.searchParams.append('dateRange', dateRange);
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
  brawlerId: number,
  date: Date,
  dateRange: DateRange,
  trophyRange: TrophyRange
): Promise<BrawlersRankStatistics[]> {
  const response = await fetchGetBattleEventBrawlersRankStatistics(
    eventId, brawlerId, date, dateRange, trophyRange,
    {revalidate: 5 * 60}
  );

  if (!response.ok) {
    throw await ApiError.create(response);
  }

  const data = await response.json() as ListResponse<BrawlersRankStatistics>;
  return data.content
    .map(stat => {
      const sortedBrawlerIds = [brawlerId, ...stat.brawlerIds.filter(id => id != brawlerId)];
      return {
        ...stat,
        brawlerIds: sortedBrawlerIds
      };
    })
    .sort((a, b) => a.averageRank - b.averageRank);
}


export async function fetchGetBrawlerResultStatistics(
  date: Date,
  dateRange: DateRange,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/brawler-result`);
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  url.searchParams.append('dateRange', dateRange);
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
  dateRange: DateRange,
  trophyRange: TrophyRange | null,
  soloRankTierRange: SoloRankTierRange | null
): Promise<BrawlerResultStatistics[]> {
  const response = await fetchGetBrawlerResultStatistics(
    date, dateRange, trophyRange, soloRankTierRange,
    {revalidate: 5 * 60}
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
  dateRange: DateRange,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/brawlers/${brawlerId}/result`);
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  url.searchParams.append('dateRange', dateRange);
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
  dateRange: DateRange,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null
): Promise<BattleEventResultStatistics[]> {
  const response = await fetchGetBrawlerBattleEventResultStatistics(
    brawlerId, date, dateRange, trophyRange, soloRankTierRange,
    {revalidate: 5 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BattleEventResultStatisticsResponse>;
    return data.content
      .sort((a, b) => b.winRate - a.winRate)
      .map(stat => ({
        event: {
          id: stat.event.id,
          mode: stat.event.mode,
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
  dateRange: DateRange,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/brawlers/${brawlerId}/brawlers-result`);
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  url.searchParams.append('dateRange', dateRange);
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
  dateRange: DateRange,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null
): Promise<BrawlersResultStatistics[]> {
  const response = await fetchGetBrawlerBrawlersResultStatistics(
    brawlerId, date, dateRange, trophyRange, soloRankTierRange,
    {revalidate: 5 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlersResultStatistics>;
    return data.content.map(stat => {
      const sortedBrawlerIds = [brawlerId, ...stat.brawlerIds.filter(id => id != brawlerId)];
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
  dateRange: DateRange,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/brawlers/${brawlerId}/enemy-result`);
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  url.searchParams.append('dateRange', dateRange);
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
  dateRange: DateRange,
  trophyRange?: TrophyRange | null,
  soloRankTierRange?: SoloRankTierRange | null
): Promise<BrawlerEnemyResultStatistics[]> {
  const response = await fetchGetBrawlerEnemyResultStatistics(
    brawlerId, date, dateRange, trophyRange, soloRankTierRange,
    {revalidate: 5 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlerEnemyResultStatistics>;
    return data.content.sort((a, b) => a.winRate - b.winRate);
  }

  throw await ApiError.create(response);
}

export async function fetchGetBrawlerOwnershipRate(brawlerId: number, trophyRange: TrophyRange, options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/brawlers/${brawlerId}/ownership`);
  url.searchParams.append('trophyRange', trophyRange);
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

export async function getBrawlerOwnershipRate(brawlerId: number, trophyRange: TrophyRange): Promise<BrawlerItemOwnership> {
  const response = await fetchGetBrawlerOwnershipRate(brawlerId, trophyRange, {revalidate: 5 * 60});

  if (!response.ok) {
    throw await ApiError.create(response);
  }

  return await response.json() as BrawlerItemOwnership;
}
