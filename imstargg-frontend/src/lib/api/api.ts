import {TrophyRange} from "@/model/enums/TrophyRange";
import {SoloRankTierRange} from "@/model/enums/SoloRankTierRange";

const BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;

interface CacheOptions {
  revalidate?: number | false;
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

export async function fetchGetRenewalStatus(tag: string): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/players/${tag}/renewal-status`);
  return await fetch(url);
}

export async function fetchGetNewPlayerRenewalStatus(tag: string): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/players/${tag}/renewal-status-new`);
  return await fetch(url);
}

export async function fetchRenewPlayer(tag: string): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/players/${tag}/renew`);

  return await fetch(url, {
    method: 'POST',
  });
}

export async function fetchRenewNewPlayer(tag: string): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/players/${tag}/renew-new`);

  return await fetch(url, {
    method: 'POST',
  });
}

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

export async function fetchGetBattles(tag: string, page: number = 1, options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/players/${tag}/battles`);
  url.searchParams.append('page', page.toString());
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['players', tag, 'battles', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function fetchGetBrawlers(options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/brawlstars/brawlers`);
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['brawlers'],
      revalidate: options.revalidate
    }
  });
}

export async function fetchGetBrawlStarsNews(page: number, options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/brawlstars/news`);
  url.searchParams.append('language', 'KOREAN');
  url.searchParams.append('page', page.toString());
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['brawlstars', 'news', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function fetchGetBattleEvents(date: Date, options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/brawlstars/events`);
  url.searchParams.append('date', date.toISOString().split('T')[0]);
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['brawlstars', 'events', url.searchParams.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function fetchGetBattleEvent(id: number, options?: CacheOptions): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/brawlstars/events/${id}`);
  if (!options) {
    return await fetch(url);
  }

  return await fetch(url, {
    next: {
      tags: ['brawlstars', 'events', id.toString()],
      revalidate: options.revalidate
    }
  });
}

export async function fetchGetBattleEventBrawlerResultStatistics(
  eventId: number,
  date: Date,
  duplicateBrawler: boolean,
  trophyRange?: TrophyRange,
  soloRankTierRange?: SoloRankTierRange,
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

export async function fetchGetBattleEventBrawlersResultStatistics(
  eventId: number,
  date: Date,
  duplicateBrawler: boolean,
  trophyRange?: TrophyRange,
  soloRankTierRange?: SoloRankTierRange,
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
