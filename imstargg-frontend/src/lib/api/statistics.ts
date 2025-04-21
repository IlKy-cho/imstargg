import {TrophyRange} from "@/model/enums/TrophyRange";
import {SoloRankTierRange} from "@/model/enums/SoloRankTierRange";
import {BrawlerResultStatistics} from "@/model/statistics/BrawlerResultStatistics";
import {BrawlerRankStatistics} from "@/model/statistics/BrawlerRankStatistics";
import {BrawlersRankStatistics} from "@/model/statistics/BrawlersRankStatistics";
import {BrawlerPairResultStatistics} from "@/model/statistics/BrawlerPairResultStatistics";
import {BrawlerEnemyResultStatistics} from "@/model/statistics/BrawlerEnemyResultStatistics";
import {BattleEventResultStatistics} from "@/model/statistics/BattleEventResultStatistics";
import {BattleEventMode} from "@/model/enums/BattleEventMode";
import {ApiError, BASE_URL, CacheOptions, ListResponse} from "@/lib/api/api";
import {BrawlerItemOwnership} from "@/model/statistics/BrawlerItemOwnership";
import {DateRange, DateRangeValue} from "@/model/enums/DateRange";
import dayjs from "dayjs";

const calculateDateRange = (date: Date, dateRange: DateRange): {startDate: string, endDate: string} => {
  const startDate = dayjs(date);
  const endDate = dayjs(date);
  const dateFormat = 'YYYY-MM-DD';
  switch (dateRange) {
    case DateRangeValue.THREE_DAYS:
      return {startDate: startDate.subtract(3, 'days').format(dateFormat), endDate: endDate.format(dateFormat)};
    case DateRangeValue.ONE_WEEK:
      return {startDate: startDate.subtract(1, 'weeks').format(dateFormat), endDate: endDate.format(dateFormat)};
    case DateRangeValue.TWO_WEEKS:
      return {startDate: startDate.subtract(2, 'weeks').format(dateFormat), endDate: endDate.format(dateFormat)};
    case DateRangeValue.ONE_MONTH:
      return {startDate: startDate.subtract(1, 'months').format(dateFormat), endDate: endDate.format(dateFormat)};
  }
}

export async function fetchGetBattleEventBrawlerResultStatistics(
  eventId: number,
  date: Date,
  dateRange: DateRange,
  trophyRange: TrophyRange | null,
  soloRankTierRange: SoloRankTierRange | null,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/events/${eventId}/result`);
  const {startDate, endDate} = calculateDateRange(date, dateRange);
  url.searchParams.append('endDate', endDate);
  url.searchParams.append('startDate', startDate);
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
      tags: ['statistics', 'events', eventId.toString(), 'result', url.searchParams.toString()],
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
  trophyRange: TrophyRange | null = null,
  soloRankTierRange: SoloRankTierRange | null = null
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
  trophyRange: TrophyRange | null,
  soloRankTierRange: SoloRankTierRange | null,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/events/${eventId}/result/pair`);
  const {startDate, endDate} = calculateDateRange(date, dateRange);
  url.searchParams.append('brawlerId', brawlerId.toString());
  url.searchParams.append('endDate', endDate);
  url.searchParams.append('startDate', startDate);
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
      tags: ['statistics', 'events', eventId.toString(), 'result', 'pair', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function getBattleEventBrawlersResultStatistics(
  eventId: number,
  brawlerId: number,
  date: Date,
  dateRange: DateRange,
  trophyRange: TrophyRange | null = null,
  soloRankTierRange: SoloRankTierRange | null = null,
): Promise<BrawlerPairResultStatistics[]> {
  const response = await fetchGetBattleEventBrawlersResultStatistics(
    eventId, brawlerId, date, dateRange, trophyRange, soloRankTierRange,
    {revalidate: 5 * 60}
  );

  if (!response.ok) {
    throw await ApiError.create(response);
  }

  const data = await response.json() as ListResponse<BrawlerPairResultStatistics>;
  return data.content
    .sort((a, b) => b.winRate - a.winRate);
}

export async function fetchGetBattleEventResultBrawlerEnemyStatistics(
  eventId: number,
  brawlerId: number,
  date: Date,
  dateRange: DateRange,
  trophyRange: TrophyRange | null,
  soloRankTierRange: SoloRankTierRange | null,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/events/${eventId}/result/enemy`);
  url.searchParams.append('brawlerId', brawlerId.toString());
  const {startDate, endDate} = calculateDateRange(date, dateRange);
  url.searchParams.append('endDate', endDate);
  url.searchParams.append('startDate', startDate);
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
      tags: ['statistics', 'events', eventId.toString(), 'result', 'enemy', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function getBattleEventResultBrawlerEnemyStatistics(
  eventId: number,
  brawlerId: number,
  date: Date,
  dateRange: DateRange,
  trophyRange: TrophyRange | null = null,
  soloRankTierRange: SoloRankTierRange | null = null
): Promise<BrawlerEnemyResultStatistics[]> {
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
  const url = new URL(`${BASE_URL}/api/v1/statistics/events/${eventId}/rank`);
  const {startDate, endDate} = calculateDateRange(date, dateRange);
  url.searchParams.append('endDate', endDate);
  url.searchParams.append('startDate', startDate);
  url.searchParams.append('trophyRange', trophyRange);
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['statistics', 'events', eventId.toString(), 'rank', url.searchParams.toString()],
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
  const url = new URL(`${BASE_URL}/api/v1/statistics/events/${eventId}/rank/pair`);
  url.searchParams.append('brawlerId', brawlerId.toString());
  const {startDate, endDate} = calculateDateRange(date, dateRange);
  url.searchParams.append('endDate', endDate);
  url.searchParams.append('startDate', startDate);
  url.searchParams.append('trophyRange', trophyRange);
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['statistics', 'events', eventId.toString(), 'rank', 'pair', url.searchParams.toString()],
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
    .sort((a, b) => a.averageRank - b.averageRank);
}


export async function fetchGetBrawlerResultStatistics(
  date: Date,
  dateRange: DateRange,
  trophyRange: TrophyRange | null,
  soloRankTierRange: SoloRankTierRange | null,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/brawler/result`);
  const {startDate, endDate} = calculateDateRange(date, dateRange);
  url.searchParams.append('endDate', endDate);
  url.searchParams.append('startDate', startDate);
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
      tags: ['statistics', 'brawler', 'result', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function getBrawlerResultStatistics(
  date: Date,
  dateRange: DateRange,
  trophyRange: TrophyRange | null = null,
  soloRankTierRange: SoloRankTierRange | null = null
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
  trophyRange: TrophyRange | null,
  soloRankTierRange: SoloRankTierRange | null,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/brawlers/${brawlerId}/result`);
  const {startDate, endDate} = calculateDateRange(date, dateRange);
  url.searchParams.append('endDate', endDate);
  url.searchParams.append('startDate', startDate);
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
  trophyRange: TrophyRange | null = null,
  soloRankTierRange: SoloRankTierRange | null = null
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
            name: stat.event.mapName ?? null,
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
  trophyRange: TrophyRange | null,
  soloRankTierRange: SoloRankTierRange | null,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/brawlers/${brawlerId}/result/pair`);
  const {startDate, endDate} = calculateDateRange(date, dateRange);
  url.searchParams.append('endDate', endDate);
  url.searchParams.append('startDate', startDate);
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
      tags: ['statistics', 'brawlers', brawlerId.toString(), 'result', 'pair', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function getBrawlerBrawlersResultStatistics(
  brawlerId: number,
  date: Date,
  dateRange: DateRange,
  trophyRange: TrophyRange | null = null,
  soloRankTierRange: SoloRankTierRange | null = null
): Promise<BrawlerPairResultStatistics[]> {
  const response = await fetchGetBrawlerBrawlersResultStatistics(
    brawlerId, date, dateRange, trophyRange, soloRankTierRange,
    {revalidate: 5 * 60}
  );

  if (response.ok) {
    const data = await response.json() as ListResponse<BrawlerPairResultStatistics>;
    return data.content.sort((a, b) => b.winRate - a.winRate);
  }

  throw await ApiError.create(response);
}


export async function fetchGetBrawlerEnemyResultStatistics(
  brawlerId: number,
  date: Date,
  dateRange: DateRange,
  trophyRange: TrophyRange | null = null,
  soloRankTierRange: SoloRankTierRange | null = null,
  options?: CacheOptions
): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/statistics/brawlers/${brawlerId}/result/enemy`);
  const {startDate, endDate} = calculateDateRange(date, dateRange);
  url.searchParams.append('endDate', endDate);
  url.searchParams.append('startDate', startDate);
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
      tags: ['statistics', 'brawlers', brawlerId.toString(), 'result', 'enemy', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function getBrawlerEnemyResultStatistics(
  brawlerId: number,
  date: Date,
  dateRange: DateRange,
  trophyRange: TrophyRange | null = null,
  soloRankTierRange: SoloRankTierRange | null = null
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
