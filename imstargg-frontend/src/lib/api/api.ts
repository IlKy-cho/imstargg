const BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;

export class ApiError extends Error {
  constructor(
    public response: Response,
    message = `Failed to fetch from ${response.url}. status: ${response.status}, body: ${response.body}`
  ) {
    super(message);
  }
}

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
  return await fetch(url, {
    next: {
      tags: ['players', tag, 'renewal-status']
    }
  });
}

export async function fetchRenewPlayer(tag: string): Promise<Response> {
  const url = new URL(`${BASE_URL}/api/v1/players/${tag}/renew`);

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